package com.techullurgy.chessk.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DatastoreManager(
    private val dataStore: DataStore<Preferences>
) {
    val clientIdFlow = dataStore.data
        .map { it[clientIdKey] }
        .distinctUntilChanged()

    suspend fun storeClientId(clientId: String) {
        dataStore.edit {
            it[clientIdKey] = clientId
        }
    }

    companion object {
        private val clientIdKey = stringPreferencesKey("CLIENT_ID_KEY")
    }
}