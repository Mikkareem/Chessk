package com.techullurgy.chessk.feature.splash.domain.di

import com.techullurgy.chessk.feature.splash.domain.usecases.CheckLoggedInUsecase
import com.techullurgy.chessk.feature.user_details.data.di.featureUserDetailsDataModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val featureSplashDomainModule = module {
    includes(featureUserDetailsDataModule)

    singleOf(::CheckLoggedInUsecase)
}