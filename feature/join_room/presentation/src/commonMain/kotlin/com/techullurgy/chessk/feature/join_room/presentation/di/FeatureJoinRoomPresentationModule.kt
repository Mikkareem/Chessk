package com.techullurgy.chessk.feature.join_room.presentation.di

import com.techullurgy.chessk.feature.join_room.domain.di.featureJoinRoomDomainModule
import com.techullurgy.chessk.feature.join_room.presentation.viewmodels.JoinRoomViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureJoinRoomPresentationModule = module {
    includes(featureJoinRoomDomainModule)

    viewModelOf(::JoinRoomViewModel)
}