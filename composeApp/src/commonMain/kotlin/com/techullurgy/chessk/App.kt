package com.techullurgy.chessk

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techullurgy.chessk.core.remote.coreRemoteModule
import com.techullurgy.chessk.navigation.AppNavigation
import com.techullurgy.chessk.navigation.appModule
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication

@Composable
fun App(
    platformApplication: KoinApplication.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    KoinApplication(
        application = {
            platformApplication()
            modules(
                coreRemoteModule,
                appModule
            )
        }
    ) {
        MaterialTheme {
            Scaffold(
                modifier = modifier.fillMaxSize()
            ) {
                AppNavigation(
                    modifier = Modifier.padding(it)
                )
            }
        }
    }
}