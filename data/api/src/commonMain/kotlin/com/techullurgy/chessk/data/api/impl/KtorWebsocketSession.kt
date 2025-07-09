package com.techullurgy.chessk.data.api.impl

import com.techullurgy.chessk.data.api.WebsocketFrame
import com.techullurgy.chessk.data.api.WebsocketSession
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.serialization.WebsocketContentConverter
import io.ktor.serialization.deserialize
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

class KtorWebsocketSession(
    private val socket: DefaultClientWebSocketSession
) : WebsocketSession {

    val converter: WebsocketContentConverter? get() = socket.converter

    override suspend fun sendSerialized(value: Any) {
        socket.sendSerialized(value)
    }

    override suspend fun close() = socket.close()

    override val incoming: ReceiveChannel<WebsocketFrame> = socket.incomingAsWebsocketFrame()

    override val isActive: Boolean
        get() = socket.isActive

    override val outgoing: SendChannel<WebsocketFrame> = socket.outgoingAsWebsocketFrame()
}

private fun DefaultClientWebSocketSession.incomingAsWebsocketFrame(): ReceiveChannel<WebsocketFrame> {
    val channel = Channel<WebsocketFrame>()

    launch {
        while (true) {
            delay(50)
            if (incoming.isClosedForReceive) {
                channel.close()
            }
        }
    }

    launch {
        select {
            incoming.onReceive {
                val frame = when (it) {
                    is Frame.Text -> WebsocketFrame.Text(it.readText())
                    else -> TODO()
                }
                channel.send(frame)
            }
        }
    }

    return channel
}

private fun DefaultClientWebSocketSession.outgoingAsWebsocketFrame(): SendChannel<WebsocketFrame> {
    val channel = Channel<WebsocketFrame>()

    launch {
        while (true) {
            delay(50)
            if (outgoing.isClosedForSend) {
                channel.close()
            }
        }
    }

    launch {
        channel.receiveAsFlow().collect {
            val frame = when (it) {
                is WebsocketFrame.Text -> Frame.Text(it.value)
            }
            outgoing.send(frame)
        }
    }

    return channel
}

context(session: WebsocketSession)
suspend inline fun <reified T> WebsocketFrame.asDeserialized(): T? {
    val frame = when (this) {
        is WebsocketFrame.Text -> Frame.Text(value)
    }
    return (session as? KtorWebsocketSession)?.converter?.deserialize<T>(frame)
}