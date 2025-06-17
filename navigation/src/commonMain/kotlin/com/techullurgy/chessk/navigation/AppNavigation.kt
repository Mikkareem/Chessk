package com.techullurgy.chessk.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.techullurgy.chessk.feature.game.api.navigation.gameNavigation
import com.techullurgy.chessk.feature.user_details.api.navigation.UserDetails
import com.techullurgy.chessk.feature.user_details.api.navigation.userDetailsNavigation

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = UserDetails
    ) {
        userDetailsNavigation(navController)
        gameNavigation(navController)
    }
}