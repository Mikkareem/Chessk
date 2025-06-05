package com.techullurgy.chessk.core.ui.photo_picker.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSItemProvider
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.getBytes
import platform.Photos.PHPhotoLibrary
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerConfigurationAssetRepresentationModeCurrent
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.darwin.NSObject

internal class PhotoPickerDelegate(
    private val scope: CoroutineScope,
    private val onResult: (ByteArray?) -> Unit
): NSObject(), PHPickerViewControllerDelegateProtocol {
    override fun picker(
        picker: PHPickerViewController,
        didFinishPicking: List<*>
    ) {
        scope.launch {
            val result = didFinishPicking
                .mapNotNull {
                    val result = it as? PHPickerResult ?: return@mapNotNull null

                    async {
                        result.itemProvider.loadFileRepresentationForTypeIdentifierSuspend()
                    }
                }
                .awaitAll()
                .filterNotNull()
                .first()

            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }

        picker.dismissViewControllerAnimated(true, null)
    }
}

// Extension function to convert NSData to ByteArray
@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    return ByteArray(this@toByteArray.length.toInt()).apply {
        usePinned { pinned ->
            this@toByteArray.getBytes(
                buffer = pinned.addressOf(0),
                length = this@toByteArray.length
            )
        }
    }
}

private suspend fun NSItemProvider.loadFileRepresentationForTypeIdentifierSuspend(): ByteArray? =
    suspendCancellableCoroutine { continuation ->
        val progress = loadFileRepresentationForTypeIdentifier(
            typeIdentifier = registeredTypeIdentifiers.firstOrNull() as? String ?: UTTypeImage.identifier
        ) { url, error ->
            error?.let {
                continuation.resumeWith(Result.success(null))
                return@loadFileRepresentationForTypeIdentifier
            }

            val data = url?.let {
                (NSData.dataWithContentsOfURL(it) ?: it.absoluteURL()?.dataRepresentation())?.toByteArray()
            }
            continuation.resumeWith(Result.success(data))
        }

        continuation.invokeOnCancellation {
            progress.cancel()
        }
    }

internal fun createPHPickerViewController(
    delegate: PHPickerViewControllerDelegateProtocol,
): PHPickerViewController {
    val configuration = PHPickerConfiguration(PHPhotoLibrary.sharedPhotoLibrary())
    val filterList = listOf(PHPickerFilter.imagesFilter())
    val newFilter = PHPickerFilter.anyFilterMatchingSubfilters(filterList.toList())
    configuration.filter = newFilter
    configuration.preferredAssetRepresentationMode = PHPickerConfigurationAssetRepresentationModeCurrent
    configuration.selectionLimit = 1
    val picker = PHPickerViewController(configuration)
    picker.delegate = delegate
    return picker
}