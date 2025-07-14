package com.techullurgy.chessk.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.techullurgy.chessk.feature.game_room.presentation.screens.GameRoomScreenRoot
import com.techullurgy.chessk.feature.joined_rooms.presentation.screens.JoinedGamesScreenRoot
import com.techullurgy.chessk.feature.login.presentation.screens.LoginUserScreenRoot
import com.techullurgy.chessk.feature.register.presentation.screens.RegisterUserScreenRoot

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
        composable<RegisterRoute> {
            RegisterUserScreenRoot(
                onLoginClick = {
                    navController.navigate(LoginRoute)
                },
                onSuccess = {
                    navController.navigate(MenuRoute) {
                        popUpTo(navController.graph.findStartDestination().route!!) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<LoginRoute> {
            LoginUserScreenRoot(
                onRegisterClick = {
                    navController.navigate(RegisterRoute)
                },
                onSuccess = {
                    navController.navigate(MenuRoute) {
                        popUpTo(navController.graph.findStartDestination().route!!) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<MenuRoute> { }

        composable<CreateRoomRoute> { }

        composable<CreatedRoomsRoute> { }

        composable<JoinRoomRoute> { }

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