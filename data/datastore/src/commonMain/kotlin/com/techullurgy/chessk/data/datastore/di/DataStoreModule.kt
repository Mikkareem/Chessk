package com.techullurgy.chessk.data.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.techullurgy.chessk.data.datastore.DatastoreManager
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformDataStoreModule: Module

val dataStoreModule = module {
    includes(platformDataStoreModule)

    single { DatastoreManager(get<DataStore<Preferences>>()) }
}