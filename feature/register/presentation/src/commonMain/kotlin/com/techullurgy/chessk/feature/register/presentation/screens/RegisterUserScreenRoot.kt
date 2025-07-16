package com.techullurgy.chessk.feature.register.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.chessk.core.ui.components.ChessKButton
import com.techullurgy.chessk.core.ui.components.ChessKInputField
import com.techullurgy.chessk.feature.register.presentation.viewmodels.RegisterUserUiAction
import com.techullurgy.chessk.feature.register.presentation.viewmodels.RegisterUserViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterUserScreenRoot(
    onLoginClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val viewModel = koinViewModel<RegisterUserViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        var firstTime = true
        snapshotFlow { state }
            .collectLatest {
                if (firstTime) {
                    firstTime = false
                } else {
                    if (!it.isRegistering && it.error.isBlank()) onSuccess()
                }
            }
    }

    RegisterUserScreen(
        onRegister = { name, email, password ->
            viewModel.onAction(RegisterUserUiAction.OnRegister(name, email, password))
        },
        onLoginClick = onLoginClick,
        error = state.error
    )
}

@Composable
private fun RegisterUserScreen(
    onRegister: (String, String, String) -> Unit,
    onLoginClick: () -> Unit,
    error: String? = null
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        error?.let {
            Text(it, color = Color.Red)
        }
        ChessKInputField(
            value = username,
            onValueChange = { username = it },
            label = "Username"
        )
        ChessKInputField(
            value = email,
            onValueChange = { email = it },
            label = "Email"
        )
        ChessKInputField(
            value = password,
            onValueChange = { password = it },
            label = "Password"
        )

        ChessKButton(
            onClick = {
                onRegister(username, email, password)
            },
            text = "Register"
        )

        ChessKButton(
            onClick = onLoginClick,
            text = "I want to Login"
        )
    }
}