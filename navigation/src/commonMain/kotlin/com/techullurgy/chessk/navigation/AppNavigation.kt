package com.techullurgy.chessk.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.techullurgy.chessk.feature.game_room.presentation.screens.GameRoomScreenRoot
import com.techullurgy.chessk.feature.joined_rooms.presentation.screens.JoinedGamesScreenRoot

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = JoinedGamesRoute
    ) {
        composable<JoinedGamesRoute> {
            JoinedGamesScreenRoot(
                onGameClick = {
                    navController.navigate(GameRoomRoute(it))
                }
            )
        }

        composable<GameRoomRoute> {
            GameRoomScreenRoot(it.toRoute<GameRoomRoute>().roomId)
        }
    }
}