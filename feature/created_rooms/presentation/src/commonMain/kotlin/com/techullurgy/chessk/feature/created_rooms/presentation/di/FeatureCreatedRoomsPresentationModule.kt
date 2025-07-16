package com.techullurgy.chessk.feature.created_rooms.presentation.di

import com.techullurgy.chessk.feature.created_rooms.domain.di.featureCreatedRoomsDomainModule
import com.techullurgy.chessk.feature.created_rooms.presentation.viewmodels.CreatedRoomsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureCreatedRoomsPresentationModule = module {
    includes(featureCreatedRoomsDomainModule)

    viewModelOf(::CreatedRoomsViewModel)
}