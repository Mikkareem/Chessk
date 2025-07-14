package com.techullurgy.chessk.feature.login.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.chessk.base.AppResult
import com.techullurgy.chessk.feature.login.domain.usecases.LoginUserUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

internal class LoginUserViewModel(
    private val loginUserUsecase: LoginUserUsecase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUserUiState())
    val state = _state.asStateFlow()

    private fun loginUser(email: String, password: String) {
        loginUserUsecase(email, password)
            .onEach { r ->
                when (r) {
                    AppResult.Loading -> {
                        _state.update {
                            it.copy(
                                isLoggingIn = true,
                                error = ""
                            )
                        }
                    }

                    is AppResult.Success<*> -> {
                        _state.update {
                            it.copy(
                                isLoggingIn = false,
                                error = ""
                            )
                        }
                    }

                    else -> {
                        _state.update {
                            it.copy(
                                isLoggingIn = false,
                                error = "Error Occurred"
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: LoginUserUiAction) {
        when (action) {
            is LoginUserUiAction.OnLogin -> TODO()
        }
    }
}

internal data class LoginUserUiState(
    val isLoggingIn: Boolean = false,
    val error: String = ""
)

internal sealed interface LoginUserUiAction {
    data class OnLogin(val email: String, val password: String) : LoginUserUiAction
}