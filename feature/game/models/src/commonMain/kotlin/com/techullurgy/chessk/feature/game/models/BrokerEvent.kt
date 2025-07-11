package com.techullurgy.chessk.feature.game.models

sealed interface BrokerEvent {
    data object BrokerConnectedEvent : BrokerEvent
    data object BrokerNotConnectedEvent : BrokerEvent
    data object BrokerLoadingEvent : BrokerEvent
    data object BrokerRefreshingEvent : BrokerEvent
    data object BrokerRefreshedEvent : BrokerEvent
    data object BrokerRetryingEvent : BrokerEvent
    data object BrokerRetriedEvent : BrokerEvent
}