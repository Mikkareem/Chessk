package com.techullurgy.chessk.data.api

import kotlinx.coroutines.flow.Flow

interface WebsocketSession<SC, CS> {
    val isActive: Boolean
    val incoming: Flow<SC?>

    suspend fun close()
}

sealed interface WebsocketFrame {
    data class Text(val value: String) : WebsocketFrame
}