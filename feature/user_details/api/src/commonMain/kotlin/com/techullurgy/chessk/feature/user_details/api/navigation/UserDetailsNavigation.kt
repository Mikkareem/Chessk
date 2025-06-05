package com.techullurgy.chessk.feature.user_details.api.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.techullurgy.chessk.feature.user_details.presentation.profile_picture_upload.ProfilePicUploadScreen
import kotlinx.serialization.Serializable

@Serializable
data object UserDetails

@Serializable
internal data object ProfilePicUpdate

fun NavGraphBuilder.userDetailsNavigation(
    navController: NavHostController
) {
    navigation<UserDetails>(
        startDestination = ProfilePicUpdate
    ) {
        composable<ProfilePicUpdate> {
            ProfilePicUploadScreen()
        }
    }
}

fun NavHostController.navigateToUserDetails() {
    navigate(UserDetails)
}