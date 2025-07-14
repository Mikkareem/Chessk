package com.techullurgy.chessk.feature.register.domain.di

import com.techullurgy.chessk.feature.register.domain.usecases.RegisterUserUsecase
import com.techullurgy.chessk.feature.user_details.data.di.featureUserDetailsDataModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val featureRegisterDomainModule = module {
    includes(featureUserDetailsDataModule)

    singleOf(::RegisterUserUsecase)
}