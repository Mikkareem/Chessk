package com.techullurgy.chessk.feature.game.data.di

import com.techullurgy.chessk.data.database.di.databaseModule
import com.techullurgy.chessk.data.remote.di.remoteModule
import com.techullurgy.chessk.data.websockets.di.websocketsModule
import com.techullurgy.chessk.feature.game.data.GameDataSource
import com.techullurgy.chessk.feature.game.data.GameDataSourceImpl
import com.techullurgy.chessk.feature.game.data.GameRoomMessageBroker
import com.techullurgy.chessk.feature.game.data.MessageBroker
import com.techullurgy.chessk.feature.game.models.BrokerEvent
import org.koin.dsl.module

val featureGameDataModule = module {
    includes(websocketsModule, databaseModule, remoteModule)

    single<MessageBroker<BrokerEvent>> { GameRoomMessageBroker(get(), get(), get(), get()) }

    single<GameDataSource> { GameDataSourceImpl(get(), get()) }
}