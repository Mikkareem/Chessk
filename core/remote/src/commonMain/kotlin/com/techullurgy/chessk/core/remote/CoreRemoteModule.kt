package com.techullurgy.chessk.core.remote

import com.techullurgy.chessk.core.constants.Constants
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreRemoteModule = module {
    single<HttpClient>{ apiClient }

    single<HttpClient>(
        named(Constants.KoinQualifierNamedConstants.WEBSOCKET_HTTP_CLIENT)
    ) { websocketClient }
}