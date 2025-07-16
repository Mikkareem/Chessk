package com.techullurgy.chessk.navigation

import com.techullurgy.chessk.feature.create_room.presentation.di.featureCreateRoomPresentationModule
import com.techullurgy.chessk.feature.created_rooms.presentation.di.featureCreatedRoomsPresentationModule
import com.techullurgy.chessk.feature.game_room.presentation.di.featureGameRoomPresentationModule
import com.techullurgy.chessk.feature.join_room.presentation.di.featureJoinRoomPresentationModule
import com.techullurgy.chessk.feature.joined_rooms.presentation.di.featureJoinedRoomsPresentationModule
import com.techullurgy.chessk.feature.login.presentation.di.featureLoginPresentationModule
import com.techullurgy.chessk.feature.register.presentation.di.featureRegisterPresentationModule
import com.techullurgy.chessk.feature.splash.presentation.di.featureSplashPresentationModule
import org.koin.dsl.module

val appModule = module {
    includes(
        featureSplashPresentationModule,
        featureRegisterPresentationModule,
        featureLoginPresentationModule,
        featureCreateRoomPresentationModule,
        featureCreatedRoomsPresentationModule,
        featureJoinRoomPresentationModule,
        featureJoinedRoomsPresentationModule,
        featureGameRoomPresentationModule
    )
}