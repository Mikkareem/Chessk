package com.techullurgy.chessk.data.api.impl

import com.techullurgy.chessk.data.api.WebsocketFrame
import com.techullurgy.chessk.data.api.WebsocketSession
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive

internal class KtorWebsocketSession<SC, CS>(
    val socket: DefaultClientWebSocketSession,
    private val deserialize: suspend WebsocketSession<SC, CS>.(WebsocketFrame) -> SC?
) : WebsocketSession<SC, CS> {

    override suspend fun close() = socket.close()

    override val incoming: Flow<SC?> = socket.incoming.receiveAsFlow().map { frame ->
        val websocketFrame = when (frame) {
            is Frame.Text -> WebsocketFrame.Text(frame.readText())
            else -> TODO()
        }

        deserialize(websocketFrame)
    }

    override val isActive: Boolean
        get() = socket.isActive
}