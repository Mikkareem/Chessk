package com.techullurgy.chessk.feature.user_details.data.di

import com.techullurgy.chessk.core.constants.Constants
import com.techullurgy.chessk.feature.user_details.data.repository.UserDetailsRepositoryImpl
import com.techullurgy.chessk.feature.user_details.domain.repository.UserDetailsRepository
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userDetailsDataModule = module {
    single<UserDetailsRepository> {
        UserDetailsRepositoryImpl(
            authClient = get<HttpClient>(named(Constants.KoinQualifierNamedConstants.AUTH_HTTP_CLIENT))
        )
    }
}