package com.techullurgy.chessk.feature.menu.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuScreenRoot(
    onNavigateToCreateRoom: () -> Unit,
    onNavigateToJoinRoom: () -> Unit,
    onNavigateToCreatedRooms: () -> Unit,
    onNavigateToJoinedRooms: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onNavigateToCreateRoom
        ) {
            Text("Create Room")
        }

        Button(
            onClick = onNavigateToJoinRoom
        ) {
            Text("Join Room")
        }

        Button(
            onClick = onNavigateToCreatedRooms
        ) {
            Text("Created Rooms")
        }

        Button(
            onClick = onNavigateToJoinedRooms
        ) {
            Text("Joined Rooms")
        }
    }
}