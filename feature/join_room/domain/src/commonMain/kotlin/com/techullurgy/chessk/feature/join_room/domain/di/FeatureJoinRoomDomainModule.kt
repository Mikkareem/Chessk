package com.techullurgy.chessk.feature.join_room.domain.di

import com.techullurgy.chessk.feature.rooms.data.di.featureRoomsDataModule
import org.koin.dsl.module

val featureJoinRoomDomainModule = module {
    includes(featureRoomsDataModule)
}