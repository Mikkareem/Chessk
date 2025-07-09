package com.techullurgy.chessk.data.api.utils

import com.techullurgy.chessk.base.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal fun <T> safeNetworkCall(block: suspend () -> T): Flow<AppResult<T>> = flow {
    try {
        emit(AppResult.Loading)
        val result = block()
        emit(AppResult.Success(result))
    } catch (e: Exception) {
        emit(AppResult.Failure)
    }
}