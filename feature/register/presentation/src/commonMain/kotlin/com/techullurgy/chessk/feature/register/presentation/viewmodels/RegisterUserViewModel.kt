package com.techullurgy.chessk.feature.register.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.chessk.base.AppResult
import com.techullurgy.chessk.feature.register.domain.usecases.RegisterUserUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

internal class RegisterUserViewModel(
    private val registerUserUsecase: RegisterUserUsecase
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUserUiState())
    val state = _state.asStateFlow()

    private fun register(name: String, email: String, password: String) {
        registerUserUsecase(name, email, password)
            .onEach { r ->
                when (r) {
                    AppResult.Loading -> {
                        _state.update {
                            it.copy(
                                isRegistering = true,
                                error = ""
                            )
                        }
                    }

                    is AppResult.Success<*> -> {
                        _state.update {
                            it.copy(
                                isRegistering = false,
                                error = ""
                            )
                        }
                    }

                    else -> {
                        _state.update {
                            it.copy(
                                isRegistering = false,
                                error = "Error Occurred"
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: RegisterUserUiAction) {
        when (action) {
            is RegisterUserUiAction.OnRegister -> register(
                action.name,
                action.email,
                action.password
            )
        }
    }
}

internal data class RegisterUserUiState(
    val isRegistering: Boolean = false,
    val error: String = ""
)

internal sealed interface RegisterUserUiAction {
    data class OnRegister(val name: String, val email: String, val password: String) :
        RegisterUserUiAction
}