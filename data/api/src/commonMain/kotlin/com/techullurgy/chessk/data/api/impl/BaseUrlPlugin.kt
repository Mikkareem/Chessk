package com.techullurgy.chessk.data.api.impl

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.defaultRequest

internal expect val localhost: String

private const val PORT = 8080

private val appHost = localhost

internal fun HttpClientConfig<*>.baseUrlSetupForApi() {
    defaultRequest {
        url("http://$appHost:$PORT/")
    }
}

internal fun HttpClientConfig<*>.baseUrlSetupForWebsocket() {
    defaultRequest {
        url("ws://$appHost:$PORT/")
    }
}