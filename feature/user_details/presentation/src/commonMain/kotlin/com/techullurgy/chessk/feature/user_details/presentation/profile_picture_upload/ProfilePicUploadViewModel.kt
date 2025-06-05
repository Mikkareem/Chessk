package com.techullurgy.chessk.feature.user_details.presentation.profile_picture_upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.chessk.feature.user_details.domain.repository.UserDetailsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

internal class ProfilePicUploadViewModel(
    private val repository: UserDetailsRepository
): ViewModel() {

    private val _state = MutableStateFlow(ProfilePicUploadScreenState())
    val state = _state.asStateFlow()

    private fun uploadProfilePicture(picture: ByteArray) {
        repository.uploadProfilePicture(picture)
            .onStart {
                _state.update {
                    it.copy(
                        isUploading = true,
                        isUploadCompleted = false,
                        errorMessage = null,
                        progress = 0f
                    )
                }
            }
            .onCompletion { cause ->
                _state.update {
                    if(cause == null) {
                        it.copy(
                            isUploading = false,
                            isUploadCompleted = true,
                            errorMessage = null,
                            progress = 0f,
                        )
                    } else if(cause is CancellationException) {
                        it.copy(
                            isUploading = false,
                            isUploadCompleted = false,
                            errorMessage = "Upload cancelled",
                            progress = 0f,
                        )
                    } else {
                        it.copy(
                            isUploading = false,
                            isUploadCompleted = false,
                            errorMessage = cause.message,
                            progress = 0f,
                        )
                    }
                }
            }
            .onEach { progress ->
                progress.totalBytes?.let { total ->
                    _state.update {
                        it.copy(
                            progress = (progress.sentBytes / total).toFloat() * 100f
                        )
                    }
                }
            }
            .catch { cause ->
                _state.update {
                    it.copy(
                        isUploading = false,
                        isUploadCompleted = false,
                        errorMessage = cause.message,
                        progress = 0f,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ProfilePicUploadScreenAction) {
        when(action) {
            is ProfilePicUploadScreenAction.OnProfilePictureSelected -> {
                _state.update {
                    it.copy(
                        picture = action.picture
                    )
                }
            }
            ProfilePicUploadScreenAction.OnUploadClicked -> {
                state.value.picture?.let {
                    uploadProfilePicture(it)
                }
            }
        }
    }
}

internal sealed interface ProfilePicUploadScreenAction {
    class OnProfilePictureSelected(val picture: ByteArray): ProfilePicUploadScreenAction
    data object OnUploadClicked: ProfilePicUploadScreenAction
}

internal data class ProfilePicUploadScreenState(
    val picture: ByteArray? = null,
    val isUploading: Boolean = false,
    val isUploadCompleted: Boolean = false,
    val errorMessage: String? = null,
    val progress: Float = 0f,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProfilePicUploadScreenState) return false

        if (isUploading != other.isUploading) return false
        if (isUploadCompleted != other.isUploadCompleted) return false
        if (progress != other.progress) return false
        if (!picture.contentEquals(other.picture)) return false
        if (errorMessage != other.errorMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isUploading.hashCode()
        result = 31 * result + isUploadCompleted.hashCode()
        result = 31 * result + progress.hashCode()
        result = 31 * result + (picture?.contentHashCode() ?: 0)
        result = 31 * result + (errorMessage?.hashCode() ?: 0)
        return result
    }
}