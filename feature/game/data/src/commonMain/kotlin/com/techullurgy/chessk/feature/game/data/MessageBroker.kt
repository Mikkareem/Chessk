package com.techullurgy.chessk.feature.game.data

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MessageBroker<T> {
    val brokerEventsFlow: SharedFlow<T>

    val isConnected: StateFlow<Boolean>

    fun retry()
    fun refresh()
}