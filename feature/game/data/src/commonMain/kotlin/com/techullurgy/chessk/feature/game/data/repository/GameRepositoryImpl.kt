package com.techullurgy.chessk.feature.game.data.repository

import com.techullurgy.chessk.feature.game.domain.events.GameNotAvailableEvent
import com.techullurgy.chessk.feature.game.domain.events.GameNotAvailableState
import com.techullurgy.chessk.feature.game.domain.events.GameState
import com.techullurgy.chessk.feature.game.domain.events.JoinedGameState
import com.techullurgy.chessk.feature.game.domain.events.JoinedGameStateHeader
import com.techullurgy.chessk.feature.game.domain.events.JoinedGameStateHeaderList
import com.techullurgy.chessk.feature.game.domain.events.NetworkLoadingEvent
import com.techullurgy.chessk.feature.game.domain.events.NetworkLoadingState
import com.techullurgy.chessk.feature.game.domain.events.NetworkNotAvailableEvent
import com.techullurgy.chessk.feature.game.domain.events.NetworkNotAvailableState
import com.techullurgy.chessk.feature.game.domain.events.RetryFetchingEvent
import com.techullurgy.chessk.feature.game.domain.events.ServerGameEvent
import com.techullurgy.chessk.feature.game.domain.events.SomethingWentWrongEvent
import com.techullurgy.chessk.feature.game.domain.events.SomethingWentWrongState
import com.techullurgy.chessk.feature.game.domain.events.UserConnectedEvent
import com.techullurgy.chessk.feature.game.domain.events.UserDisconnectedEvent
import com.techullurgy.chessk.feature.game.domain.events.UserDisconnectedState
import com.techullurgy.chessk.feature.game.domain.repository.GameRepository
import com.techullurgy.chessk.shared.models.GameRoom
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.io.IOException
import kotlin.coroutines.coroutineContext

internal class GameRepositoryImpl(
    private val gameDataSource: GameDataSource,
    applicationScope: CoroutineScope
): GameRepository {

    private val retryEventChannel = Channel<RetryFetchingEvent>()

    private val updateEvents = getGameEventsUpdatesFlow(gameDataSource, retryEventChannel)
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NetworkLoadingEvent
        )

    @OptIn(FlowPreview::class)
    override fun getJoinedGame(roomId: String): Flow<GameState> = combine(
        updateEvents,
        gameDataSource.observeGame(roomId)
    ) { a, b ->
        if(a != null) {
            return@combine when(a) {
                NetworkLoadingEvent -> NetworkLoadingState
                NetworkNotAvailableEvent -> NetworkNotAvailableState
                UserDisconnectedEvent -> UserDisconnectedState
                GameNotAvailableEvent -> GameNotAvailableState
                SomethingWentWrongEvent -> SomethingWentWrongState
                else -> TODO()
            }
        }

        if(b == null) {
            return@combine GameNotAvailableState
        }

        JoinedGameState(
            roomId = b.roomId,
            roomName = b.roomName,
            members = emptyList(),
            board = b.board,
            assignedColor = b.assignedColor,
            lastMove = b.lastMove,
            isMyTurn = b.isMyTurn,
            availableMoves = b.availableMoves ?: emptyList(),
            cutPieces = b.cutPieces ?: emptySet(),
            yourTime = b.yourTime,
            opponentTime = b.opponentTime
        )
    }
        .debounce(70)
        .distinctUntilChanged()

    override fun getJoinedGamesList(): Flow<GameState> = combine(
        updateEvents,
        gameDataSource.observeJoinedGamesList()
    ) { a, b ->

        if(a != null) {
            return@combine when(a) {
                NetworkLoadingEvent -> NetworkLoadingState
                NetworkNotAvailableEvent -> NetworkNotAvailableState
                UserDisconnectedEvent -> UserDisconnectedState
                GameNotAvailableEvent -> GameNotAvailableState
                SomethingWentWrongEvent -> SomethingWentWrongState
                else -> TODO()
            }
        }

        val result = b.map { entity ->
            JoinedGameStateHeader(
                roomId = entity.roomId,
                roomName = entity.roomName,
                membersCount = entity.membersCount,
                isMyTurn = entity.isMyTurn,
                yourTime = entity.yourTime,
                opponentTime = entity.opponentTime
            )
        }

        JoinedGameStateHeaderList(values = result)
    }.distinctUntilChanged()

    override suspend fun createRoom(room: GameRoom) {
        gameDataSource.createRoom(room)
    }

    override suspend fun joinRoom(roomId: String) {
        gameDataSource.joinRoom(roomId)
    }

    override suspend fun getCreatedRoomsByMe(): List<GameRoom> {
        return gameDataSource.getCreatedRoomsByMe()
    }

    override suspend fun retry() {
        retryEventChannel.send(RetryFetchingEvent)
    }
}

private fun <T1, T2> Flow<T1>.withSideEffectFlow(sideEffect: Flow<T2>): Flow<T1> = callbackFlow {
    sideEffect.launchIn(this)
    collect {
        send(it)
    }
    awaitClose()
}

private fun getGameEventsUpdatesFlow(
    gameDataSource: GameDataSource,
    retryEventChannel: Channel<RetryFetchingEvent>
) = merge(
    gameDataSource.observeAndUpdateGameEventDatabase(),
    retryEventChannel.consumeAsFlow()
).transform { event ->
    when(event) {
        RetryFetchingEvent -> {
            emit(NetworkLoadingEvent)
            gameDataSource.fetchAnyJoinedRoomsAvailable()
            emit(null)
        }
        else -> emit(event)
    }
}
    .withSideEffectFlow(gameDataSource.observeWebsocketConnection())
    .onCompletion { cause ->
        try {
            gameDataSource.invalidateJoinedRooms()
        } catch (_: CancellationException) {
            withContext(NonCancellable) {
                withTimeout(5000) {
                    gameDataSource.invalidateJoinedRooms()
                }
            }
            coroutineContext.ensureActive()
        }
    }
    .transform {
        if(it !is ServerGameEvent) {
            if(it == UserConnectedEvent) {
                emit(null)
            } else {
                emit(it)
            }
        } else {
            emit(null)
        }
    }
    .onStart {
        emit(NetworkLoadingEvent)
        gameDataSource.invalidateJoinedRooms()
        gameDataSource.fetchAnyJoinedRoomsAvailable()
        emit(null)
    }
    .catch {
        if(it is IOException) {
            emit(NetworkNotAvailableEvent)
        } else if(it is NoGamesFoundException) {
            emit(GameNotAvailableEvent)
        } else {
            emit(SomethingWentWrongEvent)
        }
    }