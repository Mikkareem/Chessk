package com.techullurgy.chessk.core.ui.photo_picker

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun rememberPhotoPickerLauncher(
    onResult: (ByteArray?) -> Unit
): PhotoPickerLauncher

expect fun ByteArray.toImageBitmap(): ImageBitmap

class PhotoPickerLauncher(
    private val onLaunch: () -> Unit
) {
    fun launch() {
        onLaunch()
    }
}