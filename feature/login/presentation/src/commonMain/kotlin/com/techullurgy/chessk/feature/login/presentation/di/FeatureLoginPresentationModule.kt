package com.techullurgy.chessk.feature.login.presentation.di

import com.techullurgy.chessk.feature.login.domain.di.featureLoginDomainModule
import com.techullurgy.chessk.feature.login.presentation.viewmodels.LoginUserViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureLoginPresentationModule = module {
    includes(featureLoginDomainModule)

    viewModelOf(::LoginUserViewModel)
}