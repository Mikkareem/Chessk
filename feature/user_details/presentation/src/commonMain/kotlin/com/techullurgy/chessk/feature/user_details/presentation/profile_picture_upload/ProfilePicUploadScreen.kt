package com.techullurgy.chessk.feature.user_details.presentation.profile_picture_upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.chessk.core.ui.photo_picker.rememberPhotoPickerLauncher
import com.techullurgy.chessk.core.ui.photo_picker.toImageBitmap
import com.techullurgy.chessk.core.ui.snackbar.FailureSnackbarVisual
import com.techullurgy.chessk.core.ui.snackbar.LocalSnackbarHostState
import com.techullurgy.chessk.core.ui.snackbar.SuccessSnackbarVisual
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfilePicUploadScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<ProfilePicUploadViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    var image by remember { mutableStateOf<ImageBitmap?>(null) }

    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(state.picture) {
        state.picture?.let {
            image = withContext(Dispatchers.IO) { it.toImageBitmap() }
        }
    }

    LaunchedEffect(state.isUploadCompleted) {
        if(state.isUploadCompleted) {
            snackbarHostState.showSnackbar(
                SuccessSnackbarVisual("Profile Picture Uploaded")
            )
        }
    }

    LaunchedEffect(state.errorMessage) {
        if(state.errorMessage != null) {
            snackbarHostState.showSnackbar(
                FailureSnackbarVisual(state.errorMessage!!)
            )
        }
    }

    val photoPickerLauncher = rememberPhotoPickerLauncher {
        it?.let { picture ->
            viewModel.onAction(ProfilePicUploadScreenAction.OnProfilePictureSelected(picture))
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (image != null) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = image!!,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp).clip(RoundedCornerShape(20.dp))
                )

                if(state.isUploading) {
                    UploadingIndicator(state.progress)
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Yellow)
                    .clickable {
                        photoPickerLauncher.launch()
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Edit Profile Picture",
                    tint = Color.Black
                )
            }
        }

        if(state.picture != null) {
            Column {
                Text("Upload Completed: ${state.isUploadCompleted}")
                Text("Error: ${state.errorMessage}")
            }

            Button(
                enabled = !state.isUploading,
                onClick = {
                    viewModel.onAction(ProfilePicUploadScreenAction.OnUploadClicked)
                }
            ) {
                Text(
                    text = if(state.isUploading) "Uploading" else "Upload"
                )
            }
        }
    }
}

@Composable
private fun UploadingIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(35.dp)
            .drawBehind {
                rotate(degrees = -90f) {
                    val filledRadius = lerp(0f, 360f, progress)
                    val remainingRadius = 360f - filledRadius

                    drawCircle(
                        color = Color.Green,
                        radius = remainingRadius,
                        alpha = 0.5f
                    )
                    drawCircle(
                        color = Color.Green,
                        radius = filledRadius,
                    )
                }
            }
    ) {
        Text("${progress * 100f}%")
    }
}