package com.techullurgy.chessk.feature.game.data.api

import com.techullurgy.chessk.feature.game.domain.events.CellSelectionEvent
import com.techullurgy.chessk.feature.game.domain.events.ClientGameEvent
import com.techullurgy.chessk.feature.game.domain.events.DisconnectedEvent
import com.techullurgy.chessk.feature.game.domain.events.EnterRoomEvent
import com.techullurgy.chessk.feature.game.domain.events.GameEvent
import com.techullurgy.chessk.feature.game.domain.events.GameStartedEvent
import com.techullurgy.chessk.feature.game.domain.events.GameUpdateEvent
import com.techullurgy.chessk.feature.game.domain.events.NetworkLoadingEvent
import com.techullurgy.chessk.feature.game.domain.events.NetworkNotAvailableEvent
import com.techullurgy.chessk.feature.game.domain.events.PieceMoveEvent
import com.techullurgy.chessk.feature.game.domain.events.ResetSelectionDoneEvent
import com.techullurgy.chessk.feature.game.domain.events.ResetSelectionEvent
import com.techullurgy.chessk.feature.game.domain.events.SelectionResultEvent
import com.techullurgy.chessk.feature.game.domain.events.TimerUpdateEvent
import com.techullurgy.chessk.feature.game.domain.events.UserConnectedEvent
import com.techullurgy.chessk.feature.game.domain.events.UserDisconnectedEvent
import com.techullurgy.chessk.feature.game.domain.remote.ChessGameApi
import com.techullurgy.chessk.shared.endpoints.CreateRoomEndpoint
import com.techullurgy.chessk.shared.endpoints.GameWebsocketEndpoint
import com.techullurgy.chessk.shared.endpoints.GetCreatedRoomsEndpoint
import com.techullurgy.chessk.shared.endpoints.GetJoinedRoomsEndpoint
import com.techullurgy.chessk.shared.endpoints.JoinRoomEndpoint
import com.techullurgy.chessk.shared.events.CellSelection
import com.techullurgy.chessk.shared.events.ClientToServerBaseEvent
import com.techullurgy.chessk.shared.events.Disconnected
import com.techullurgy.chessk.shared.events.EnterRoomHandshake
import com.techullurgy.chessk.shared.events.GameStarted
import com.techullurgy.chessk.shared.events.GameUpdate
import com.techullurgy.chessk.shared.events.PieceMove
import com.techullurgy.chessk.shared.events.ResetSelection
import com.techullurgy.chessk.shared.events.ResetSelectionDone
import com.techullurgy.chessk.shared.events.SelectionResult
import com.techullurgy.chessk.shared.events.ServerToClientBaseEvent
import com.techullurgy.chessk.shared.events.TimerUpdate
import com.techullurgy.chessk.shared.models.GameRoom
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.serialization.deserialize
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal class ChessGameApiImpl(
    private val socketClient: HttpClient,
    private val apiClient: HttpClient
): ChessGameApi {

    private var canStartSession = MutableStateFlow(false)

    private var session: DefaultClientWebSocketSession? = null

    private var eventChannel: Channel<ClientToServerBaseEvent> = Channel()

    private val sessionConnectionFlow: Flow<GameEvent> = channelFlow<GameEvent> {
        send(NetworkLoadingEvent)
        launch {
            try {
                try {
                    session = socketClient.webSocketSession(GameWebsocketEndpoint.actualUrl)
                    send(UserConnectedEvent)
                } catch (e: Exception) {
                    send(NetworkNotAvailableEvent)
                    e.printStackTrace()
                    close()
                }

                launch {
                    eventChannel.consumeEach {
                        session?.sendSerialized(it)
                    }
                }

                session!!.incoming
                    .receiveAsFlow()
                    .map { session!!.converter!!.deserialize<ServerToClientBaseEvent>(it) }
                    .collect { event ->
                        send(
                            when (event) {
                                is GameStarted -> GameStartedEvent(
                                    roomId = event.roomId,
                                    members = event.members,
                                    assignedColor = event.assignedColor
                                )

                                is GameUpdate -> GameUpdateEvent(
                                    roomId = event.roomId,
                                    board = event.board,
                                    currentTurn = event.currentTurn,
                                    lastMove = event.lastMove,
                                    cutPieces = event.cutPieces,
                                    kingInCheckIndex = event.kingInCheckIndex,
                                )

                                is ResetSelectionDone -> ResetSelectionDoneEvent(event.roomId)
                                is SelectionResult -> SelectionResultEvent(
                                    roomId = event.roomId,
                                    availableMoves = event.availableMoves,
                                    selectedIndex = event.selectedIndex
                                )

                                is TimerUpdate -> TimerUpdateEvent(
                                    roomId = event.roomId,
                                    whiteTime = event.whiteTime,
                                    blackTime = event.blackTime
                                )
                            }
                        )
                    }
            } catch (e: Exception) {
                coroutineContext.ensureActive()
                send(UserDisconnectedEvent)
                e.printStackTrace()
                close()
            }
        }

        awaitClose {
            session?.let { s ->
                s.launch {
                    s.close(CloseReason(CloseReason.Codes.NORMAL, "disconnected"))
                }
            }
            session = null
            eventChannel = eventChannel.reset()
        }
    }

    override val isSocketActive: Boolean get() = session != null && session!!.isActive

    @OptIn(ExperimentalCoroutinesApi::class)
    override val gameEventsFlow: Flow<GameEvent> = canStartSession
        .flatMapLatest { enable ->
            if(enable) sessionConnectionFlow
            else flow {}
        }

    override fun startSession() {
        canStartSession.value = true
    }

    override fun stopSession() {
        canStartSession.value = false
    }

    override fun sendEvent(event: ClientGameEvent) {
        val sendableEvent = when(event) {
            is CellSelectionEvent -> CellSelection(event.roomId, event.color, event.selectedIndex)
            is DisconnectedEvent -> Disconnected(event.roomId)
            is EnterRoomEvent -> EnterRoomHandshake(event.roomId)
            is PieceMoveEvent -> PieceMove(event.roomId, event.color, event.from, event.to)
            is ResetSelectionEvent -> ResetSelection(event.roomId)
        }
        session?.launch {
            eventChannel.send(sendableEvent)
        }
    }

    override suspend fun fetchAnyJoinedRoomsAvailable(): List<GameRoom> {
        val response = apiClient.get(GetJoinedRoomsEndpoint.actualUrl)
        val rooms = response.body<List<GameRoom>>()
        return rooms
    }

    override suspend fun getCreatedRoomsByMe(): List<GameRoom> {
        apiClient.get(GetCreatedRoomsEndpoint.actualUrl)
        return emptyList()
    }

    override suspend fun joinRoom(roomId: String) {
        apiClient.post(JoinRoomEndpoint.actualUrl)
    }

    override suspend fun createRoom(room: GameRoom) {
        apiClient.post(CreateRoomEndpoint.actualUrl)
    }

    private fun <T> Channel<T>.reset(): Channel<T> {
        close()
        return Channel()
    }
}