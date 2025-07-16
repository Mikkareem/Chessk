package com.techullurgy.chessk.data.api.extensions

import com.techullurgy.chessk.data.api.ChessKApi
import com.techullurgy.chessk.data.api.WebsocketFrame
import com.techullurgy.chessk.data.api.WebsocketSession
import com.techullurgy.chessk.data.api.impl.KtorChessKApi
import com.techullurgy.chessk.data.api.impl.KtorWebsocketSession
import com.techullurgy.chessk.shared.endpoints.GameWebsocketEndpoint
import io.ktor.client.plugins.websocket.webSocketSession


@PublishedApi
internal suspend fun <SC, CS> ChessKApi.connectGameWebsocketImpl(
    deserialize: suspend WebsocketSession<SC, *>.(WebsocketFrame) -> SC?
): WebsocketSession<SC, CS> {
    val socket = (this as KtorChessKApi).wsClient.webSocketSession(GameWebsocketEndpoint.actualUrl)

    return KtorWebsocketSession(
        socket = socket,
        deserialize = deserialize
    )
}

suspend inline fun <reified SC, reified CS> ChessKApi.connectGameWebsocket(): WebsocketSession<SC, CS> =
    connectGameWebsocketImpl { it.asDeserialized() }