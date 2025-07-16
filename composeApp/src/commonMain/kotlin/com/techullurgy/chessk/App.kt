package com.techullurgy.chessk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.techullurgy.chessk.core.ui.snackbar.AppSnackbarHost
import com.techullurgy.chessk.core.ui.snackbar.LocalSnackbarHostState
import com.techullurgy.chessk.navigation.AppNavigation
import com.techullurgy.chessk.navigation.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication
import org.koin.dsl.module

@Composable
fun App(
    platformApplication: KoinApplication.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    KoinApplication(
        application = {
            platformApplication()
            modules(
                module { single { CoroutineScope(SupervisorJob()) } },
                appModule
            )
        }
    ) {
        val snackbarHostState = remember { SnackbarHostState() }

        val background = Brush.linearGradient(
            colorStops = arrayOf(
                0f to Color(0xFFC964E4),
                .5f to Color(0xFF3D4FA9),
                1f to Color(0xFF2B0B75)
            ),
            start = Offset.Zero,
            end = Offset.Infinite,
        )

        MaterialTheme {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                snackbarHost = { AppSnackbarHost(snackbarHostState) }
            ) {
                CompositionLocalProvider(
                    LocalSnackbarHostState provides snackbarHostState
                ) {
                    Box(
                        modifier = Modifier.background(background)
                    ) {
                        AppNavigation(
                            modifier = Modifier.padding(it)
                        )
                    }
                }
            }
        }
    }
}