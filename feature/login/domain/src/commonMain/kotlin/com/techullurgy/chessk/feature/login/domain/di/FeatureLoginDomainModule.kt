package com.techullurgy.chessk.feature.login.domain.di

import com.techullurgy.chessk.feature.login.domain.usecases.LoginUserUsecase
import com.techullurgy.chessk.feature.user_details.data.di.featureUserDetailsDataModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val featureLoginDomainModule = module {
    includes(featureUserDetailsDataModule)

    singleOf(::LoginUserUsecase)
}