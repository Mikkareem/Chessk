package com.techullurgy.chessk.feature.create_room.domain.di

import com.techullurgy.chessk.feature.rooms.data.di.featureRoomsDataModule
import org.koin.dsl.module

val featureCreateRoomDomainModule = module {
    includes(featureRoomsDataModule)
}