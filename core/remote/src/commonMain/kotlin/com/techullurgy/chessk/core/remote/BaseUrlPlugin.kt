package com.techullurgy.chessk.core.remote

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.defaultRequest

expect internal val host: String

private const val PORT = 8080

internal fun HttpClientConfig<*>.baseUrlSetupForApi() {
    defaultRequest {
        url("http://$host:$PORT/")
    }
}

internal fun HttpClientConfig<*>.baseUrlSetupForWebsocket() {
    defaultRequest {
        url("ws://$host:$PORT/")
    }
}