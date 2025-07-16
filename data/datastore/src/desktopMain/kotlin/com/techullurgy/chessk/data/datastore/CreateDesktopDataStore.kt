package com.techullurgy.chessk.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDesktopDataStore(): DataStore<Preferences> = createDataStore { DATASTORE_FILE_NAME }