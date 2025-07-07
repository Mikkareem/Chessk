package com.techullurgy.chessk

import com.techullurgy.chessk.domain.GameServer
import com.techullurgy.chessk.shared.endpoints.GameWebsocketEndpoint
import com.techullurgy.chessk.shared.events.CellSelection
import com.techullurgy.chessk.shared.events.ClientToServerBaseEvent
import com.techullurgy.chessk.shared.events.Disconnected
import com.techullurgy.chessk.shared.events.EnterRoomHandshake
import com.techullurgy.chessk.shared.events.PieceMove
import com.techullurgy.chessk.shared.events.ResetSelection
import com.techullurgy.chessk.shared.events.baseEventJson
import com.techullurgy.chessk.shared.utils.SharedConstants
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.converter
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json

val gameServer = GameServer()

fun Application.configureSockets() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(
            Json(from = baseEventJson) {}
        )
    }

    routing {
        route(GameWebsocketEndpoint.signature) {
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

private fun Route.standardWebsocket(
    handleFrame: suspend (
        socket: DefaultWebSocketServerSession,
        clientId: String,
        payload: ClientToServerBaseEvent
    ) -> Unit
) {
    webSocket {

        val clientId = call.parameters[SharedConstants.Parameters.CHESSK_CLIENT_ID_HEADER_KEY]

        if (clientId == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No Session found"))
            return@webSocket
        }

        try {
            gameServer.createSessionForClientId(clientId, this)

            gameServer.getJoinedRoomsForClientId(clientId)
                .forEach { it.onUserSessionEstablished(clientId) }

            incoming
                .receiveAsFlow()
                .map { converter!!.deserialize<ClientToServerBaseEvent>(it) }
                .collect { payload ->
                    handleFrame(this, clientId, payload)
                }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            gameServer.disconnect(clientId)
        }
    }
}