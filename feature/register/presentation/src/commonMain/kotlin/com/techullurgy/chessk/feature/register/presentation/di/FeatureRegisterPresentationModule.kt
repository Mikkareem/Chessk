package com.techullurgy.chessk.feature.register.presentation.di

import com.techullurgy.chessk.feature.register.domain.di.featureRegisterDomainModule
import com.techullurgy.chessk.feature.register.presentation.viewmodels.RegisterUserViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureRegisterPresentationModule = module {
    includes(featureRegisterDomainModule)

    viewModelOf(::RegisterUserViewModel)
}