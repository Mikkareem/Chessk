package com.techullurgy.chessk.feature.create_room.presentation.di

import com.techullurgy.chessk.feature.create_room.domain.di.featureCreateRoomDomainModule
import com.techullurgy.chessk.feature.create_room.presentation.viewmodels.CreateRoomViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureCreateRoomPresentationModule = module {
    includes(featureCreateRoomDomainModule)

    viewModelOf(::CreateRoomViewModel)
}