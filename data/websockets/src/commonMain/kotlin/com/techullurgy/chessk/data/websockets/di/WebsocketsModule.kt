package com.techullurgy.chessk.data.websockets.di

import com.techullurgy.chessk.data.api.ChessKApi
import com.techullurgy.chessk.data.api.di.apiModule
import com.techullurgy.chessk.data.websockets.WebsocketDataSource
import com.techullurgy.chessk.data.websockets.wsSource
import com.techullurgy.chessk.shared.events.ClientToServerBaseEvent
import com.techullurgy.chessk.shared.events.ServerToClientBaseEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.koin.dsl.module

val websocketsModule = module {
    includes(apiModule)

    // TODO: Needs to Change CoroutineScope from get(named)
    single<WebsocketDataSource<ServerToClientBaseEvent, ClientToServerBaseEvent>> {
        wsSource(CoroutineScope(Job()), get<ChessKApi>())
    }
}