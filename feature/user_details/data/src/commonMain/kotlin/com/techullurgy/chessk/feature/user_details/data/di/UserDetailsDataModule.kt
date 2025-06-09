package com.techullurgy.chessk.feature.user_details.data.di

import com.techullurgy.chessk.feature.user_details.data.repository.UserDetailsRepositoryImpl
import com.techullurgy.chessk.feature.user_details.domain.repository.UserDetailsRepository
import io.ktor.client.HttpClient
import org.koin.dsl.module

val userDetailsDataModule = module {
    single<UserDetailsRepository> {
        UserDetailsRepositoryImpl(
            apiClient = get<HttpClient>()
        )
    }
}