package com.techullurgy.chessk.data.api.impl.clients

import com.techullurgy.chessk.data.api.impl.baseUrlSetupForApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal val authClient = HttpClient {
    baseUrlSetupForApi()
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            }
        )
    }
}