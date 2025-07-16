package com.techullurgy.chessk.feature.splash.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.chessk.feature.splash.domain.usecases.CheckLoggedInUsecase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

internal class SplashViewModel(
    checkLoggedInUsecase: CheckLoggedInUsecase
) : ViewModel() {
    val state = checkLoggedInUsecase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}