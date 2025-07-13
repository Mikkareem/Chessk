package com.techullurgy.chessk.feature.joined_rooms.domain.usecases

import com.techullurgy.chessk.feature.game.data.GameDataSource
import com.techullurgy.chessk.feature.game.models.BrokerEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ObservingGamesListUsecase(
    private val gameDataSource: GameDataSource
) {
    operator fun invoke(): Flow<GameRoomPreviewsState> = combine(
        gameDataSource.observeJoinedGames().map { list ->
            list.map {
                GameRoomPreview(
                    roomId = it.roomId,
                    roomName = it.roomName,
                    createdBy = it.createdBy,
                    yourTime = it.yourTime,
                    opponentTime = it.opponentTime
                )
            }
        },
        gameDataSource.broker.isConnected,
        gameDataSource.broker.brokerEventsFlow
    ) { previews, isConnected, event ->
        val state = GameRoomPreviewsState(
            previews = previews,
            isConnected = isConnected
        )
        when (event) {
            BrokerEvent.BrokerConnectedEvent -> state.copy(isLoading = false)
            BrokerEvent.BrokerFailureEvent -> state.copy(isLoading = false)
            BrokerEvent.BrokerLoadingEvent -> state.copy(isLoading = true)
            BrokerEvent.BrokerRefreshedEvent -> state.copy(isRefreshing = false)
            BrokerEvent.BrokerRefreshingEvent -> state.copy(isRefreshing = true)
            BrokerEvent.BrokerRetriedEvent -> state.copy(isRetrying = false)
            BrokerEvent.BrokerRetryingEvent -> state.copy(isRetrying = true)
        }
    }
}

data class GameRoomPreviewsState(
    val previews: List<GameRoomPreview>,
    val isConnected: Boolean,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isRetrying: Boolean = false
)

data class GameRoomPreview(
    val roomId: String,
    val roomName: String,
    val createdBy: String,
    val yourTime: Long,
    val opponentTime: Long
)