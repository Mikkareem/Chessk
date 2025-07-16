package com.techullurgy.chessk.data.remote.di

import com.techullurgy.chessk.data.api.ChessKApi
import com.techullurgy.chessk.data.api.di.apiModule
import com.techullurgy.chessk.data.remote.RemoteDataSource
import org.koin.dsl.module

val remoteModule = module {
    includes(apiModule)

    single { RemoteDataSource(get<ChessKApi>()) }
}