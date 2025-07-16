package com.techullurgy.chessk.feature.created_rooms.domain.di

import com.techullurgy.chessk.feature.rooms.data.di.featureRoomsDataModule
import org.koin.dsl.module

val featureCreatedRoomsDomainModule = module {
    includes(featureRoomsDataModule)
}