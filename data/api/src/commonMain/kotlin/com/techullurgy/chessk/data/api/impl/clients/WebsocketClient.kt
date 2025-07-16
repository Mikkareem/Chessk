package com.techullurgy.chessk.data.api.impl.clients

import com.techullurgy.chessk.data.api.impl.KtorClientIdInterceptorPlugin
import com.techullurgy.chessk.data.api.impl.baseUrlSetupForWebsocket
import com.techullurgy.chessk.shared.events.baseEventJson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.serialization.json.Json

internal val websocketClient = HttpClient {
    baseUrlSetupForWebsocket()
    install(KtorClientIdInterceptorPlugin.plugin)
    install(HttpCookies)
    install(WebSockets) {
        pingIntervalMillis = 20_000
        contentConverter = KotlinxWebsocketSerializationConverter(
            Json(from = baseEventJson) {}
        )
    }
}