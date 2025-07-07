package com.techullurgy.chessk.core.models

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Failure(val error: String) : AppResult<Nothing>
    data object Loading : AppResult<Nothing>
}