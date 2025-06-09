package dev.techullurgy.chess

import dev.techullurgy.chess.domain.GameServer
import dev.techullurgy.chess.domain.decodeBaseModel
import dev.techullurgy.chess.domain.getType
import dev.techullurgy.chess.events.*
import dev.techullurgy.chess.events.constants.BaseEventConstants
import dev.techullurgy.chess.sessions.GameSession
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

val gameServer = GameServer()

fun Application.configureSockets() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }

    routing {
        route("/join/ws") {
            standardWebsocket { socket, clientId, payload ->
                when(payload) {
                    is EnterRoomHandshake -> {
                        val room = gameServer.getRoomById(roomId = payload.roomId) ?: return@standardWebsocket
                        room.playerEntered(clientId)
                    }
                    is PieceMove -> {
                        val room = gameServer.getRoomById(roomId = payload.roomId) ?: return@standardWebsocket
                        room.movePiece(payload)
                    }
                    is Disconnected -> TODO()
                    is CellSelection -> {
                        val room = gameServer.getRoomById(roomId = payload.roomId) ?: return@standardWebsocket
                        room.cellSelectedForMove(payload)
                    }

                    is ResetSelection -> {
                        val room = gameServer.getRoomById(roomId = payload.roomId) ?: return@standardWebsocket
                        room.resetSelection()
                    }
                }
            }
        }
    }
}

fun Route.standardWebsocket(
    handleFrame: suspend (
        socket: DefaultWebSocketServerSession,
        clientId: String,
        payload: ReceiverBaseEvent
    ) -> Unit
) {
    webSocket {
        val session = call.sessions.get<GameSession>()
        if(session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No Session found"))
            return@webSocket
        }

        try {
            gameServer.createSessionForClientId(session.clientId, this)
            for(frame in incoming) {
                if(frame is Frame.Text) {
                    val message = frame.readText()
                    val messageType = message.getType()

                    val payload = when(messageType) {
                        BaseEventConstants.TYPE_ENTER_ROOM_HANDSHAKE -> decodeBaseModel<EnterRoomHandshake>(message)
                        BaseEventConstants.TYPE_CELL_SELECTION -> decodeBaseModel<CellSelection>(message)
                        BaseEventConstants.TYPE_PIECE_MOVE -> decodeBaseModel<PieceMove>(message)
                        BaseEventConstants.TYPE_RESET_SELECTION -> decodeBaseModel<ResetSelection>(message)
                        BaseEventConstants.TYPE_DISCONNECT -> decodeBaseModel<Disconnected>(message)
                        else -> TODO()
                    }

                    handleFrame(this, session.clientId, payload)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            gameServer.disconnect(session.clientId)
        }
    }
}