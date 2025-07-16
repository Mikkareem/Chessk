package com.techullurgy.chessk.data.api.di

import com.techullurgy.chessk.data.api.ChessKApi
import com.techullurgy.chessk.data.api.impl.KtorChessKApi
import com.techullurgy.chessk.data.api.impl.clients.apiClient
import com.techullurgy.chessk.data.api.impl.clients.authClient
import com.techullurgy.chessk.data.api.impl.clients.websocketClient
import com.techullurgy.chessk.data.datastore.di.dataStoreModule
import org.koin.dsl.module

val apiModule = module {
    includes(dataStoreModule)

    single<ChessKApi> {
        KtorChessKApi(
            httpClient = apiClient,
            wsClient = websocketClient,
            noAuthHttpClient = authClient
        )
    }
}