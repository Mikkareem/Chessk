package com.techullurgy.chessk.feature.user_details.presentation.profile_picture_upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.chessk.core.ui.photo_picker.rememberPhotoPickerLauncher
import com.techullurgy.chessk.core.ui.photo_picker.toImageBitmap
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfilePicUploadScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<ProfilePicUploadViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    val photoPickerLauncher = rememberPhotoPickerLauncher {
        it?.let { picture ->
            viewModel.onAction(ProfilePicUploadScreenAction.OnProfilePictureSelected(picture))
        }
    }

    Column(
        modifier = modifier.fillMaxSize().background(Color.Magenta),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.picture != null) {
            Image(
                bitmap = state.picture!!.toImageBitmap(),
                contentDescription = "Profile Pic"
            )
        }

        Button(
            onClick = {
                photoPickerLauncher.launch()
            }
        ) {
            Text("Pick One")
        }

        if(state.picture != null) {
            Column {
                Text("Uploading: ${state.isUploading}")
                Text("Upload Completed: ${state.isUploadCompleted}")
                Text("Error: ${state.errorMessage}")
                Text("Progress: ${state.progress}")
            }

            Button(
                onClick = {
                    viewModel.onAction(ProfilePicUploadScreenAction.OnUploadClicked)
                }
            ) {
                Text("Upload")
            }
        }
    }
}