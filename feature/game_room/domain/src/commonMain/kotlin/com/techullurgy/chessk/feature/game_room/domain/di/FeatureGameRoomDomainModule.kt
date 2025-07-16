package com.techullurgy.chessk.feature.game_room.domain.di

import com.techullurgy.chessk.feature.game.data.di.featureGameDataModule
import com.techullurgy.chessk.feature.game_room.domain.usecases.ObservingGameUsecase
import org.koin.dsl.module

val featureGameRoomDomainModule = module {
    includes(featureGameDataModule)

    single { ObservingGameUsecase(get()) }
}