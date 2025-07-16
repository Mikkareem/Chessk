package com.techullurgy.chessk.core.ui.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided in this context")
}

@Composable
fun AppSnackbarHost(
    snackbarHostState: SnackbarHostState,
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            val visual = data.visuals as? SnackbarVisual ?: return@SnackbarHost

            val containerColor = when(visual) {
                is FailureSnackbarVisual -> MaterialTheme.colorScheme.errorContainer
                is SuccessSnackbarVisual -> Color.Green
            }

            val contentColor = when(visual) {
                is FailureSnackbarVisual -> MaterialTheme.colorScheme.error
                is SuccessSnackbarVisual -> Color.Black
            }

            val message = when(visual) {
                is FailureSnackbarVisual -> visual.message
                is SuccessSnackbarVisual -> visual.message
            }

            Snackbar(
                containerColor = containerColor,
                contentColor = contentColor,
                action = {
                    TextButton(
                        onClick = { data.dismiss() }
                    ) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text(message)
            }
        }
    )
}