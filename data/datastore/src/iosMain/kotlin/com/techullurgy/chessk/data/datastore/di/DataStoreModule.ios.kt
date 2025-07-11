package com.techullurgy.chessk.data.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.techullurgy.chessk.data.datastore.createIosDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataStoreModule: Module = module {
    single<DataStore<Preferences>> { createIosDataStore() }
}