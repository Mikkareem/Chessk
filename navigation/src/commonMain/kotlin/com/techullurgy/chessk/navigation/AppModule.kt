package com.techullurgy.chessk.navigation

import com.techullurgy.chessk.feature.game_room.presentation.di.featureGameRoomPresentationModule
import com.techullurgy.chessk.feature.joined_rooms.presentation.di.featureJoinedRoomsPresentationModule
import com.techullurgy.chessk.feature.login.presentation.di.featureLoginPresentationModule
import com.techullurgy.chessk.feature.register.presentation.di.featureRegisterPresentationModule
import org.koin.dsl.module

val appModule = module {
    includes(
        featureRegisterPresentationModule,
        featureLoginPresentationModule,
        featureJoinedRoomsPresentationModule,
        featureGameRoomPresentationModule
    )
}