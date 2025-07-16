package com.techullurgy.chessk.feature.splash.presentation.di

import com.techullurgy.chessk.feature.splash.domain.di.featureSplashDomainModule
import com.techullurgy.chessk.feature.splash.presentation.viewmodels.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureSplashPresentationModule = module {
    includes(featureSplashDomainModule)

    viewModelOf(::SplashViewModel)
}