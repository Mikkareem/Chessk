package com.techullurgy.chessk.feature.user_details.data.di

import com.techullurgy.chessk.data.datastore.di.dataStoreModule
import com.techullurgy.chessk.data.remote.di.remoteModule
import com.techullurgy.chessk.feature.user_details.data.repository.UserDetailsRepository
import com.techullurgy.chessk.feature.user_details.data.repository.UserDetailsRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val featureUserDetailsDataModule = module {
    includes(dataStoreModule, remoteModule)

    singleOf(::UserDetailsRepositoryImpl) bind UserDetailsRepository::class
}