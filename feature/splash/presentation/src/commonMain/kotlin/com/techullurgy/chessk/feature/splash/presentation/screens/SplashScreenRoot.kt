package com.techullurgy.chessk.feature.splash.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.chessk.feature.splash.presentation.viewmodels.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreenRoot(
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    val viewModel = koinViewModel<SplashViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        snapshotFlow { state }
            .collectLatest {
                it?.let { isLoggedIn ->
                    delay(5000)
                    if (isLoggedIn) {
                        onSuccess()
                    } else {
                        onFailure()
                    }
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Splash...")
    }
}