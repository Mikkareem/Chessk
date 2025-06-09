package com.techullurgy.chessk.core.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

sealed class SnackbarVisual(
    final override val actionLabel: String?,
): SnackbarVisuals {
    final override val withDismissAction: Boolean = false
    final override val duration: SnackbarDuration = SnackbarDuration.Long
}

data class SuccessSnackbarVisual(
    override val message: String
): SnackbarVisual(actionLabel = null)

data class FailureSnackbarVisual(
    override val message: String
): SnackbarVisual(actionLabel = "Retry")