package com.techullurgy.chessk.data.websockets

import com.techullurgy.chessk.base.AppResult
import com.techullurgy.chessk.data.api.ChessKApi
import com.techullurgy.chessk.data.api.WebsocketSession
import com.techullurgy.chessk.data.api.extensions.connectGameWebsocket
import com.techullurgy.chessk.data.api.extensions.sendSerialized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

interface WebsocketDataSource<SC, CS> {
    val isConnected: StateFlow<Boolean>

    val eventsFlow: SharedFlow<AppResult<SC?>>

    fun startSession()
    fun stopSession()

    fun send(e: CS)
}

internal open class ChessKWebsocketDataSource<SC, CS>(
    scope: CoroutineScope,
    private val connect: suspend () -> WebsocketSession<SC, CS>?,
    private val send: suspend WebsocketSession<SC, CS>.(CS) -> Unit
) : WebsocketDataSource<SC, CS> {
    private lateinit var outgoingChannel: Channel<CS>

    private val canConnectFlow = MutableStateFlow(false)

    private val _isConnected = MutableStateFlow(false)
    override val isConnected = _isConnected.asStateFlow()

    private var session: WebsocketSession<SC, CS>? = null

    private val _eventsFlow = channelFlow<AppResult<SC?>> {
        canConnectFlow
            .dropWhile { !it }
            .collectLatest { enabled ->
                if (enabled) {
                    send(AppResult.Loading)
                    try {
                        session = connect()

                        if (session == null) {
                            send(AppResult.Failure)
                            return@collectLatest
                        }

                        _isConnected.value = true
                        send(AppResult.Success(null))

                        launch {
                            outgoingChannel.receiveAsFlow().collectLatest {
                                session?.send(it)
                            }
                        }

                        session?.incoming?.collect {
                            send(AppResult.Success(it!!))
                        }
                    } finally {
                        session?.close()
                        session = null
                        _isConnected.value = false
                    }
                }
            }
    }
        .onStart { emit(AppResult.Loading) }
        .catch { emit(AppResult.Failure) }

    override val eventsFlow = _eventsFlow
        .distinctUntilChanged()
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed()
        )

    override fun startSession() {
        if (canConnectFlow.value) return

        if (::outgoingChannel.isInitialized) {
            outgoingChannel.close()
        }
        outgoingChannel = Channel()
        canConnectFlow.value = true
    }

    override fun stopSession() {
        if (!canConnectFlow.value) return

        outgoingChannel.close()
        canConnectFlow.value = false
    }

    override fun send(e: CS) {
        outgoingChannel.trySend(e)
    }
}

internal inline fun <reified SC, reified CS> wsSource(
    scope: CoroutineScope,
    api: ChessKApi
): WebsocketDataSource<SC, CS> = ChessKWebsocketDataSource(
    scope = scope,
    connect = { api.connectGameWebsocket() },
    send = { sendSerialized(it) }
)