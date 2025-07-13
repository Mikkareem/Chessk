package com.techullurgy.chessk.feature.joined_rooms.presentation.di

import com.techullurgy.chessk.feature.joined_rooms.domain.di.featureJoinedRoomsDomainModule
import com.techullurgy.chessk.feature.joined_rooms.presentation.viewmodels.JoinedGamesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureJoinedRoomsPresentationModule = module {
    includes(featureJoinedRoomsDomainModule)

    viewModelOf(::JoinedGamesViewModel)
}