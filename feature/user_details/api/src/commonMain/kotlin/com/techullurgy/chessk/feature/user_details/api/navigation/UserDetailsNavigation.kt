package com.techullurgy.chessk.feature.user_details.api.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.techullurgy.chessk.feature.user_details.presentation.edit.UserDetailsEditScreen
import com.techullurgy.chessk.feature.user_details.presentation.profile_picture_upload.ProfilePicUploadScreen
import kotlinx.serialization.Serializable

@Serializable
data object UserDetails

@Serializable
internal data object EditUserDetails

@Serializable
internal data object ProfilePicUpdate

fun NavGraphBuilder.userDetailsNavigation(
    navController: NavHostController
) {
    navigation<UserDetails>(
        startDestination = EditUserDetails
    ) {
        composable<EditUserDetails> {
            UserDetailsEditScreen(
                onProfilePictureEditClick = {
                    navController.navigate(ProfilePicUpdate)
                }
            )
        }

        composable<ProfilePicUpdate> {
            ProfilePicUploadScreen()
        }
    }
}

fun NavHostController.navigateToUserDetails() {
    navigate(UserDetails)
}