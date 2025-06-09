package com.techullurgy.chessk.core.ui.photo_picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.uikit.LocalUIViewController
import com.techullurgy.chessk.core.ui.photo_picker.utils.PhotoPickerDelegate
import com.techullurgy.chessk.core.ui.photo_picker.utils.createPHPickerViewController
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image

@Composable
actual fun rememberPhotoPickerLauncher(
    onResult: (ByteArray?) -> Unit
): PhotoPickerLauncher {
    val scope = rememberCoroutineScope()
    val currentViewController = LocalUIViewController.current

    val delegate = remember {
        PhotoPickerDelegate(scope, onResult)
    }

    return remember(currentViewController) {
        PhotoPickerLauncher {
            val controller = createPHPickerViewController(delegate)
            currentViewController.presentViewController(controller, true, null)
        }
    }
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return try {
        Bitmap.makeFromImage(Image.makeFromEncoded(this)).asComposeImageBitmap()
    } catch (_: Exception) {
        ImageBitmap(10, 10)
    }
}