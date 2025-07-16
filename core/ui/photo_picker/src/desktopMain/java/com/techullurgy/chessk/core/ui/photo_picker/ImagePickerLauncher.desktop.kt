package com.techullurgy.chessk.core.ui.photo_picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
actual fun rememberPhotoPickerLauncher(onResult: (ByteArray?) -> Unit): PhotoPickerLauncher {
    return remember {
        PhotoPickerLauncher(
            onLaunch = {
                showPickerInSwing()?.let { file ->
                    onResult(file.readBytes())
                }
            }
        )
    }
}

private fun showPickerInSwing(): File? {
    return JFileChooser().apply {
        dialogTitle = "Pick a File"
        fileFilter = FileNameExtensionFilter(
            "Image files",
            "jpg", "jpeg", "png"
        )
    }.takeIf { it.showOpenDialog(null) == JFileChooser.APPROVE_OPTION }
        ?.selectedFile
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}