package com.techullurgy.chessk.feature.game_room.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.chessk.feature.game.models.GameRoom
import com.techullurgy.chessk.feature.game_room.domain.usecases.ObservingGameUsecase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GameRoomViewModel(
    observingGameUsecase: ObservingGameUsecase,
) : ViewModel() {

    private val observedStates = observingGameUsecase("")
        .map {
            GameRoomUiState(
                room = it.room,
                isConnected = it.isConnected,
                isLoading = it.isLoading,
                isRefreshing = it.isRefreshing,
                isRetrying = it.isRetrying,
            )
        }

    val state = observedStates
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GameRoomUiState()
        )
}

data class GameRoomUiState(
    val room: GameRoom? = null,
    val isConnected: Boolean = false,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isRetrying: Boolean = false
)