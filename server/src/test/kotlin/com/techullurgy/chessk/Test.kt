package com.techullurgy.chessk

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

private data class GameEntityProjection(val event: String)

private sealed interface ServerEvent {
    data object GameUpdateEvent : ServerEvent
    data object TimerUpdateEvent : ServerEvent
    data object ResetSelectionDoneEvent : ServerEvent
}

private sealed interface SocketEvent {
    data object GameUpdateEvent : SocketEvent
    data object TimerUpdateEvent : SocketEvent
    data object ResetSelectionDoneEvent : SocketEvent
}

private sealed interface GameDomainEvent<out T> {
    data object NetworkLoadingEvent : GameDomainEvent<Nothing>
    data object ConnectionClosedEvent : GameDomainEvent<Nothing>
    data class SinkEvent<T>(val sink: T) : GameDomainEvent<T>
}


private val db1 = Channel<GameEntityProjection>()
private val db2 = Channel<List<GameEntityProjection>>()

private fun observeJoinedGame() = db1.receiveAsFlow()
private fun observeJoinedGamesList() = db2.receiveAsFlow()

private sealed interface AppResult<out T> {
    data object Loading : AppResult<Nothing>
    data class Failure(val cause: Throwable?) : AppResult<Nothing>
    data class Success<T>(val data: T) : AppResult<T>
}

private const val COUNT_TOTAL = 5

@OptIn(ExperimentalCoroutinesApi::class)
private fun CoroutineScope.serverSocketChannel() = produce<ServerEvent> {

    launch {
        var count = COUNT_TOTAL
        while (count-- >= 0) {
            send(ServerEvent.TimerUpdateEvent)
            delay(1000)
        }
    }
    launch {
        var count = COUNT_TOTAL
        while (count-- >= 0) {
            send(ServerEvent.GameUpdateEvent)
            delay(Random.nextLong(2000, 3000))
        }
    }

    invokeOnClose {
//        println("Server Socket is closed")
    }
}

private val CoroutineScope.gameWebsocketFlow: SharedFlow<AppResult<SocketEvent>>
    get() {
        val serverSocket = serverSocketChannel()

        return channelFlow<AppResult<SocketEvent>> {
            send(AppResult.Loading)

            serverSocket.receiveAsFlow()
                .onEach {
                    val event = when (it) {
                        ServerEvent.GameUpdateEvent -> SocketEvent.GameUpdateEvent
                        ServerEvent.ResetSelectionDoneEvent -> SocketEvent.ResetSelectionDoneEvent
                        ServerEvent.TimerUpdateEvent -> SocketEvent.TimerUpdateEvent
                    }

                    send(AppResult.Success(event))
                }
                .catch { send(AppResult.Failure(it)) }
                .onCompletion {
                    it.ifNull {
                        send(AppResult.Failure(RuntimeException("Connection Closed")))
                        close()
                    }
                }
                .launchIn(this)

            awaitClose()
        }
            .onStart {
                println("GameWebsocketFlow Started")
            }
            .shareIn(
                scope = this,
                started = SharingStarted.WhileSubscribed(5000),
            )

    }

private sealed interface FlowParameter<out T> {
    data object ValueNotPresent : FlowParameter<Nothing>
    data class ValuePresent<T>(val value: T) : FlowParameter<T>

    fun <R> valueIfAvailable(
        whenNotPresent: () -> R = { TODO() },
        whenPresent: (T) -> R
    ): R {
        return when (this) {
            ValueNotPresent -> whenNotPresent()
            is ValuePresent<T> -> whenPresent(value)
        }
    }
}

private fun <T1, T2, R> mergedFlow(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    block: (FlowParameter<T1>, FlowParameter<T2>) -> R
): Flow<R> = callbackFlow {
    val completedCount = MutableStateFlow(0)

    launch {
        flow1.collect {
            send(block(FlowParameter.ValuePresent(it), FlowParameter.ValueNotPresent))
        }
    }.invokeOnCompletion {
        completedCount.value = completedCount.value + 1
        println("Merge1 completed")
    }

    launch {
        flow2.collect {
            send(block(FlowParameter.ValueNotPresent, FlowParameter.ValuePresent(it)))
        }
    }.invokeOnCompletion {
        completedCount.value = completedCount.value + 1
        println("Merge2 completed")
    }

    completedCount.collect {
        if (it >= 2) {
            close()
        }
    }

    awaitClose()
}

fun main2() {
    runBlocking {

        val iFlow = gameWebsocketFlow

        val updateEventsFlow1 = iFlow
            .transform {
                when (it) {
                    is AppResult.Success<SocketEvent> -> {
                        val event = it.data.toString()
                        db1.send(GameEntityProjection(event))
                    }

                    else -> emit(it)
                }
            }

        val updateEventsFlow2 = iFlow
            .transform {
                when (it) {
                    is AppResult.Success<SocketEvent> -> {
                        val event = it.data.toString()
                        db2.send(listOf(GameEntityProjection(event)))
                    }

                    else -> emit(it)
                }
            }

        val job1 = launch {
            mergedFlow(
                updateEventsFlow1,
                observeJoinedGame()
            ) { a, b ->

                a.valueIfAvailable(
                    whenNotPresent = {
                        b.valueIfAvailable {
                            GameDomainEvent.SinkEvent(it)
                        }
                    }
                ) {
                    when (it) {
                        is AppResult.Failure -> GameDomainEvent.ConnectionClosedEvent
                        AppResult.Loading -> GameDomainEvent.NetworkLoadingEvent
                        is AppResult.Success<*> -> TODO()
                    }
                }
            }
                .onCompletion { println("GameScreen Completed") }
                .collect {
                    println("GameScreen: $it")
                    if (it is GameDomainEvent.ConnectionClosedEvent) {
                        db1.close()
                    }
                }
        }

        val job2 = launch {
            mergedFlow(
                updateEventsFlow2,
                observeJoinedGamesList()
            ) { a, b ->

                a.valueIfAvailable(
                    whenNotPresent = {
                        b.valueIfAvailable {
                            GameDomainEvent.SinkEvent(it)
                        }
                    }
                ) {
                    when (it) {
                        is AppResult.Failure -> GameDomainEvent.ConnectionClosedEvent
                        AppResult.Loading -> GameDomainEvent.NetworkLoadingEvent
                        is AppResult.Success<*> -> TODO()
                    }
                }
            }.onCompletion { println("GameListScreen Completed") }.collect {
                println("GameListScreen: $it")
                if (it is GameDomainEvent.ConnectionClosedEvent) {
                    db2.close()
                }
            }
        }
    }
}

private inline fun <T> T?.ifNull(block: () -> Unit) {
    if (this == null) block.invoke()
}

fun main() {
    runBlocking {
        val numbersFlow = flow {
            repeat(COUNT_TOTAL) {
                delay(1000)
                emit(it + 1)
            }
        }
            .onCompletion { println("Upstream Completed") }
            .shareIn(
                scope = this,
                started = SharingStarted.WhileSubscribed(2000)
            )
            .onCompletion { println("Downstream Completed") }

        val job = launch {
            numbersFlow.collect {
                println("UI: $it")
            }
        }
        job.invokeOnCompletion {
            println("Collection completed")
        }


        launch {
            delay(7000)
            job.cancel()
            println("Completed Program")
        }

    }
}