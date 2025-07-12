package com.techullurgy.chessk.feature.game_room.presentation.di

import com.techullurgy.chessk.feature.game_room.domain.di.featureGameRoomDomainModule
import org.koin.dsl.module

val featureGameRoomPresentationModule = module {
    includes(featureGameRoomDomainModule)
}