package com.techullurgy.chessk.core.ui.photo_picker.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSItemProvider
import platform.Foundation.NSLocalizedDescriptionKey
import platform.Foundation.dataWithContentsOfURL
import platform.Photos.PHPhotoLibrary
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerConfigurationAssetRepresentationModeCurrent
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerModeCompact
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.darwin.NSObject
import platform.posix.memcpy

internal class PhotoPickerDelegate(
    private val scope: CoroutineScope,
    private val onResult: (ByteArray?) -> Unit
): NSObject(), PHPickerViewControllerDelegateProtocol {
    override fun picker(
        picker: PHPickerViewController,
        didFinishPicking: List<*>
    ) {
        scope.launch(Dispatchers.IO) {
            try {
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
            } catch (e: Exception) {
                e.printStackTrace()
                coroutineContext.ensureActive()
            }
        }

        picker.dismissViewControllerAnimated(true, null)
    }
}

// Extension function to convert NSData to ByteArray
@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val aLength = if(length > Int.MAX_VALUE.toUInt()) Int.MAX_VALUE else length.toInt()
    return ByteArray(aLength).apply {
        usePinned { pinned ->
            memcpy(pinned.addressOf(0), bytes, length)
        }
    }
}

private suspend fun NSItemProvider.loadFileRepresentationForTypeIdentifierSuspend(): ByteArray? =
    suspendCancellableCoroutine { continuation ->
        val progress = loadFileRepresentationForTypeIdentifier(
            typeIdentifier = registeredTypeIdentifiers.firstOrNull() as? String ?: UTTypeImage.identifier
        ) { url, error ->
            error?.let {
                val domain = it.domain
                val code = it.code
                val message = it.userInfo.get(NSLocalizedDescriptionKey) as? String

                println("Error Start")
                println("Error domain: $domain")
                println("Error code: $code")
                println("Error message: $message")
                println("Error End")

                continuation.resumeWith(Result.success(null))
                return@loadFileRepresentationForTypeIdentifier
            }

            val data = url?.let {
                NSData.dataWithContentsOfURL(it)?.toByteArray() ?: ByteArray(0)
            } ?: ByteArray(0)

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
    val filterList = listOf(
        PHPickerFilter.imagesFilter(),
        PHPickerFilter.screenshotsFilter()
    )
    val newFilter = PHPickerFilter.anyFilterMatchingSubfilters(filterList)
    configuration.filter = newFilter
    configuration.preferredAssetRepresentationMode = PHPickerConfigurationAssetRepresentationModeCurrent
    configuration.selectionLimit = 1
    val picker = PHPickerViewController(configuration)
    picker.delegate = delegate
    return picker
}