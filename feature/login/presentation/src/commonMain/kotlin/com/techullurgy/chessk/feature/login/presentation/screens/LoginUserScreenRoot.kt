package com.techullurgy.chessk.feature.login.presentation.screens

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
import com.techullurgy.chessk.feature.login.presentation.viewmodels.LoginUserUiAction
import com.techullurgy.chessk.feature.login.presentation.viewmodels.LoginUserViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginUserScreenRoot(
    onRegisterClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val viewModel = koinViewModel<LoginUserViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        var firstTime = true
        snapshotFlow { state }
            .collectLatest {
                if (firstTime) {
                    firstTime = false
                } else {
                    if (!it.isLoggingIn && it.error.isBlank()) onSuccess()
                }
            }
    }

    LoginUserScreen(
        onLogin = { email, password ->
            viewModel.onAction(LoginUserUiAction.OnLogin(email, password))
        },
        onRegisterClick = onRegisterClick,
        error = state.error
    )
}

@Composable
private fun LoginUserScreen(
    onLogin: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    error: String? = null
) {
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
                onLogin(email, password)
            },
            text = "Login"
        )

        ChessKButton(
            onClick = onRegisterClick,
            text = "I want to Register"
        )
    }
}