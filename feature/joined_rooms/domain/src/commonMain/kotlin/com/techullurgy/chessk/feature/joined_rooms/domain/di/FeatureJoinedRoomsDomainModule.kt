package com.techullurgy.chessk.feature.joined_rooms.domain.di

import com.techullurgy.chessk.feature.game.data.di.featureGameDataModule
import com.techullurgy.chessk.feature.joined_rooms.domain.usecases.ObservingGamesListUsecase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val featureJoinedRoomsDomainModule = module {
    includes(featureGameDataModule)

    singleOf(::ObservingGamesListUsecase)
}