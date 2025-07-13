package com.techullurgy.chessk.feature.game_room.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.chessk.feature.game_room.presentation.viewmodels.GameRoomViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun GameRoomScreenRoot(
    roomId: String
) {
    val viewModel = koinViewModel<GameRoomViewModel>(
        parameters = { parametersOf(roomId) }
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$state")
    }
}