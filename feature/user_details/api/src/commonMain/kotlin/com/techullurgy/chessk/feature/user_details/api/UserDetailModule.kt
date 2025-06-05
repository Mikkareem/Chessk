package com.techullurgy.chessk.feature.user_details.api

import com.techullurgy.chessk.feature.user_details.data.di.userDetailsDataModule
import com.techullurgy.chessk.feature.user_details.domain.di.userDetailsDomainModule
import com.techullurgy.chessk.feature.user_details.presentation.di.userDetailsPresentationModule
import org.koin.dsl.module

val userDetailModule = module {
    includes(
        userDetailsDomainModule,
        userDetailsDataModule,
        userDetailsPresentationModule
    )
}