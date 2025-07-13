package com.techullurgy.chessk.navigation

import com.techullurgy.chessk.feature.game_room.presentation.di.featureGameRoomPresentationModule
import com.techullurgy.chessk.feature.joined_rooms.presentation.di.featureJoinedRoomsPresentationModule
import org.koin.dsl.module

val appModule = module {
    includes(
        featureJoinedRoomsPresentationModule,
        featureGameRoomPresentationModule
    )
}