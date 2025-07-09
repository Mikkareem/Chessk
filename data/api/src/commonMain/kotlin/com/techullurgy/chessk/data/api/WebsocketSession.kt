package com.techullurgy.chessk.data.api

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

interface WebsocketSession {
    val incoming: ReceiveChannel<WebsocketFrame>
    val outgoing: SendChannel<WebsocketFrame>

    val isActive: Boolean

    suspend fun sendSerialized(value: Any)

    suspend fun close()
}

sealed interface WebsocketFrame {
    data class Text(val value: String) : WebsocketFrame
}