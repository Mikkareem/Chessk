package com.techullurgy.chessk.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.techullurgy.chessk.feature.create_room.presentation.screens.CreateRoomScreenRoot
import com.techullurgy.chessk.feature.created_rooms.presentation.screens.CreatedRoomsScreenRoot
import com.techullurgy.chessk.feature.game_room.presentation.screens.GameRoomScreenRoot
import com.techullurgy.chessk.feature.join_room.presentation.screens.JoinRoomScreenRoot
import com.techullurgy.chessk.feature.joined_rooms.presentation.screens.JoinedGamesScreenRoot
import com.techullurgy.chessk.feature.login.presentation.screens.LoginUserScreenRoot
import com.techullurgy.chessk.feature.menu.presentation.screens.MenuScreenRoot
import com.techullurgy.chessk.feature.register.presentation.screens.RegisterUserScreenRoot
import com.techullurgy.chessk.feature.splash.presentation.screens.SplashScreenRoot

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SplashRoute
    ) {
        composable<SplashRoute> {
            SplashScreenRoot(
                onSuccess = {
                    navController.navigate(MenuRoute) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onFailure = {
                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<RegisterRoute> {
            RegisterUserScreenRoot(
                onLoginClick = {
                    navController.navigate(LoginRoute)
                },
                onSuccess = {
                    navController.navigate(MenuRoute) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
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
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<MenuRoute> {
            MenuScreenRoot(
                onNavigateToCreateRoom = { navController.navigate(CreateRoomRoute) },
                onNavigateToJoinRoom = { navController.navigate(JoinRoomRoute) },
                onNavigateToCreatedRooms = { navController.navigate(CreatedRoomsRoute) },
                onNavigateToJoinedRooms = { navController.navigate(JoinedGamesRoute) },
            )
        }

        composable<CreateRoomRoute> {
            CreateRoomScreenRoot()
        }

        composable<CreatedRoomsRoute> {
            CreatedRoomsScreenRoot()
        }

        composable<JoinRoomRoute> {
            JoinRoomScreenRoot()
        }

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