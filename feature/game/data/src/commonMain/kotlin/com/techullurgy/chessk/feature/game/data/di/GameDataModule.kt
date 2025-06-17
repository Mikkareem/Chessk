package com.techullurgy.chessk.feature.game.data.di

import com.techullurgy.chessk.core.constants.Constants
import com.techullurgy.chessk.feature.game.data.api.ChessGameApiImpl
import com.techullurgy.chessk.feature.game.data.api.GameApiDataSource
import com.techullurgy.chessk.feature.game.data.db.GameDbDataSource
import com.techullurgy.chessk.feature.game.data.repository.GameDataSource
import com.techullurgy.chessk.feature.game.data.repository.GameRepositoryImpl
import com.techullurgy.chessk.feature.game.domain.remote.ChessGameApi
import com.techullurgy.chessk.feature.game.domain.repository.GameRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val gameDataModule = module {
    single<ChessGameApi> {
        ChessGameApiImpl(
            socketClient = get<HttpClient>(named(Constants.KoinQualifierNamedConstants.WEBSOCKET_HTTP_CLIENT)),
            apiClient = get<HttpClient>(),
        )
    }

    single(named(Constants.KoinQualifierNamedConstants.APP_COROUTINE_SCOPE)) {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }

    singleOf(::GameDbDataSource)
    singleOf(::GameApiDataSource)
    singleOf(::GameDataSource)

    single<GameRepository> {
        GameRepositoryImpl(get(), get(named(Constants.KoinQualifierNamedConstants.APP_COROUTINE_SCOPE)))
    }
}