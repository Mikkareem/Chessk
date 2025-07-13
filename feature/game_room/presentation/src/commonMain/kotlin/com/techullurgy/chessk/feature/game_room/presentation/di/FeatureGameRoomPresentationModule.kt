package com.techullurgy.chessk.feature.game_room.presentation.di

import com.techullurgy.chessk.feature.game_room.domain.di.featureGameRoomDomainModule
import com.techullurgy.chessk.feature.game_room.presentation.viewmodels.GameRoomViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureGameRoomPresentationModule = module {
    includes(featureGameRoomDomainModule)

    viewModelOf(::GameRoomViewModel)
}