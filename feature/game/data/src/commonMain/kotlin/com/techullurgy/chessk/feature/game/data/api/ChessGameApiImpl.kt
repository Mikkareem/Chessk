package com.techullurgy.chessk.feature.game.data.api

import com.techullurgy.chessk.core.utils.toBoardPieces
import com.techullurgy.chessk.core.utils.toCutPieces
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
import com.techullurgy.chessk.shared.events.clientToServerBaseEventJson
import com.techullurgy.chessk.shared.events.serverToClientBaseEventJson
import com.techullurgy.chessk.shared.models.GameRoom
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
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
                    session = socketClient.webSocketSession(
                        urlString = "${ChessGameApi.Companion.WS_BASE_URL}/join/ws"
                    )
                    send(UserConnectedEvent)
                } catch (e: Exception) {
                    send(NetworkNotAvailableEvent)
                    e.printStackTrace()
                    close()
                }

                launch {
                    eventChannel.consumeEach {
                        val text = clientToServerBaseEventJson.encodeToString(it)
                        val frame = Frame.Text(text)
                        session?.outgoing?.send(frame)
                    }
                }

                session!!.incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        val event = serverToClientBaseEventJson.decodeFromString<ServerToClientBaseEvent>(text)

                        send(
                            when (event) {
                                is GameStarted -> GameStartedEvent(
                                    roomId = event.roomId,
                                    members = event.members,
                                    assignedColor = event.assignedColor
                                )

                                is GameUpdate -> GameUpdateEvent(
                                    roomId = event.roomId,
                                    board = event.board.toBoardPieces(),
                                    currentTurn = event.currentTurn,
                                    lastMove = event.lastMove,
                                    cutPieces = event.cutPieces?.toCutPieces(),
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
        val response = apiClient.get("${ChessGameApi.Companion.HTTP_BASE_URL}/rooms/joined")
        val rooms = response.body<List<GameRoom>>()
        return rooms
    }

    override suspend fun getCreatedRoomsByMe(): List<GameRoom> {
        TODO("Not yet implemented")
    }

    override suspend fun joinRoom(roomId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createRoom(room: GameRoom) {
        TODO("Not yet implemented")
    }

    private fun <T> Channel<T>.reset(): Channel<T> {
        close()
        return Channel()
    }
}