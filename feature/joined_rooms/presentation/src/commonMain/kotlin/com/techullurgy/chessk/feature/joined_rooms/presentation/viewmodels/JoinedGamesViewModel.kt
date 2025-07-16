package com.techullurgy.chessk.feature.joined_rooms.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.chessk.feature.joined_rooms.domain.usecases.GameRoomPreview
import com.techullurgy.chessk.feature.joined_rooms.domain.usecases.ObservingGamesListUsecase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class JoinedGamesViewModel(
    private val observingGamesListUsecase: ObservingGamesListUsecase
) : ViewModel() {

    private val observedStates = observingGamesListUsecase().map {
        JoinedGamesUiState(
            previews = it.previews,
            isConnected = it.isConnected,
            isLoading = it.isLoading,
            isRefreshing = it.isRefreshing,
            isRetrying = it.isRetrying
        )
    }

    val state = observedStates
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = JoinedGamesUiState()
        )
}

data class JoinedGamesUiState(
    val previews: List<GameRoomPreview> = emptyList(),
    val isConnected: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isRetrying: Boolean = false
)