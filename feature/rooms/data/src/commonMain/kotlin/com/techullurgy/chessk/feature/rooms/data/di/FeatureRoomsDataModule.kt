package com.techullurgy.chessk.feature.rooms.data.di

import com.techullurgy.chessk.data.remote.di.remoteModule
import org.koin.dsl.module

val featureRoomsDataModule = module {
    includes(remoteModule)
}