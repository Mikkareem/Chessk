package com.techullurgy.chessk.feature.user_details.domain.di

import com.techullurgy.chessk.feature.user_details.domain.usecases.RegisterUserUsecase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userDetailsDomainModule = module {
    singleOf(::RegisterUserUsecase)
}