package com.techullurgy.chessk.data.api.extensions

import com.techullurgy.chessk.data.api.WebsocketFrame
import com.techullurgy.chessk.data.api.WebsocketSession
import com.techullurgy.chessk.data.api.impl.KtorWebsocketSession
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.serialization.deserialize
import io.ktor.websocket.Frame


@PublishedApi
context(session: WebsocketSession<T, *>)
internal suspend fun <T> WebsocketFrame.asDeserializedImpl(
    deserialize: suspend DefaultClientWebSocketSession.(Frame) -> T?
): T? {
    val frame: Frame = when (this) {
        is WebsocketFrame.Text -> Frame.Text(value)
    }
    return (session as? KtorWebsocketSession<T, *>)?.socket?.deserialize(frame)
}

@PublishedApi
context(session: WebsocketSession<T, *>)
internal suspend inline fun <reified T> WebsocketFrame.asDeserialized(): T? =
    asDeserializedImpl { this.converter?.deserialize<T>(it) }

@PublishedApi
internal suspend fun <T> WebsocketSession<*, T>.sendSerializedImpl(
    data: T,
    serialize: suspend DefaultClientWebSocketSession.(T) -> Unit
) {
    (this as KtorWebsocketSession<*, T>).socket.serialize(data)
}

suspend inline fun <reified T> WebsocketSession<*, T>.sendSerialized(data: T) =
    sendSerializedImpl(data) { sendSerialized(it) }