package com.techullurgy.chessk.feature.joined_rooms.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.chessk.feature.joined_rooms.presentation.viewmodels.JoinedGamesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun JoinedGamesScreenRoot(
    onGameClick: (String) -> Unit
) {
    val viewModel = koinViewModel<JoinedGamesViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.previews.isEmpty()) {
            Text("No Joined Games Available")
        } else {
            LazyColumn {
                items(state.previews) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { onGameClick(it.roomId) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(it.roomName)
                    }
                }
            }
        }
    }
}