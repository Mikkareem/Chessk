package com.techullurgy.chessk.feature.game_room.domain.usecases

import com.techullurgy.chessk.feature.game.data.GameDataSource
import com.techullurgy.chessk.feature.game.models.BrokerEvent
import com.techullurgy.chessk.feature.game.models.GameRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObservingGameUsecase(
    private val gameDataSource: GameDataSource
) {
    operator fun invoke(roomId: String): Flow<GameRoomState> = combine(
        gameDataSource.observeGame(roomId),
        gameDataSource.broker.isConnected,
        gameDataSource.broker.brokerEventsFlow
    ) { gameRoom, isConnected, event ->
        val state = GameRoomState(
            room = gameRoom,
            isConnected = isConnected
        )
        when (event) {
            BrokerEvent.BrokerConnectedEvent -> state.copy(isLoading = false)
            BrokerEvent.BrokerLoadingEvent -> state.copy(isLoading = true)
            BrokerEvent.BrokerFailureEvent -> state.copy(isLoading = false)
            BrokerEvent.BrokerRefreshedEvent -> state.copy(isRefreshing = false)
            BrokerEvent.BrokerRefreshingEvent -> state.copy(isRefreshing = true)
            BrokerEvent.BrokerRetriedEvent -> state.copy(isRetrying = true)
            BrokerEvent.BrokerRetryingEvent -> state.copy(isRetrying = false)
        }
    }
}

data class GameRoomState(
    val room: GameRoom?,
    val isConnected: Boolean,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isRetrying: Boolean = false,
)