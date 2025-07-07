package com.techullurgy.chessk.core.utils

import com.techullurgy.chessk.core.models.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

private fun <T> Flow<T>.asAppResult(): Flow<AppResult<T>> {
    return map<T, AppResult<T>> { AppResult.Success(it) }
        .onStart { emit(AppResult.Loading) }
        .catch { emit(AppResult.Failure(it.message ?: "Something Went Wrong")) }
}

fun <T> appResultFlow(block: suspend () -> T): Flow<AppResult<T>> =
    flow { emit(block()) }.asAppResult()

fun <T> Flow<AppResult<T>>.onSuccess(action: suspend (T) -> Unit): Flow<AppResult<T>> =
    AppResultSuccessCollector(this, action)

fun <T> Flow<AppResult<T>>.onFailure(action: suspend (String) -> Unit): Flow<AppResult<T>> =
    AppResultFailureCollector(this, action)

fun <T> Flow<AppResult<T>>.onLoading(action: suspend () -> Unit): Flow<AppResult<T>> =
    AppResultLoadingCollector(this, action)

fun <T> Flow<AppResult<T>>.interceptSuccess(action: suspend (T) -> Unit): Flow<AppResult<T>> =
    AppResultSuccessCollector(this, action, true)

fun <T> Flow<AppResult<T>>.interceptFailure(action: suspend (String) -> Unit): Flow<AppResult<T>> =
    AppResultFailureCollector(this, action, true)

fun <T> Flow<AppResult<T>>.interceptLoading(action: suspend () -> Unit): Flow<AppResult<T>> =
    AppResultLoadingCollector(this, action, true)

fun <T, R> Flow<AppResult<T>>.transformAppResult(
    onLoading: suspend () -> AppResult<R> = { AppResult.Loading },
    onFailure: suspend (String) -> AppResult<R> = { AppResult.Failure(it) },
    onSuccess: suspend (T) -> AppResult<R>
): Flow<AppResult<R>> = AppResultTransformCollector(this, onSuccess, onFailure, onLoading)

private class AppResultTransformCollector<T, R>(
    private val upstream: Flow<AppResult<T>>,
    private val onSuccess: suspend (T) -> AppResult<R>,
    private val onFailure: suspend (String) -> AppResult<R>,
    private val onLoading: suspend () -> AppResult<R>
) : Flow<AppResult<R>> {
    override suspend fun collect(collector: FlowCollector<AppResult<R>>) {
        upstream.collect { result ->
            when (result) {
                is AppResult.Failure -> {
                    val value = onFailure(result.error)
                    collector.emit(value)
                }

                AppResult.Loading -> {
                    val value = onLoading()
                    collector.emit(value)
                }

                is AppResult.Success<T> -> {
                    val value = onSuccess(result.data)
                    collector.emit(value)
                }
            }
        }
    }
}

private class AppResultSuccessCollector<T>(
    private val upstream: Flow<AppResult<T>>,
    private val onSuccess: suspend (T) -> Unit,
    private val interceptable: Boolean = false
) : Flow<AppResult<T>> {
    override suspend fun collect(collector: FlowCollector<AppResult<T>>) {
        upstream.collect { result ->
            if (result is AppResult.Success<T>) {
                onSuccess(result.data)
                if (interceptable) {
                    collector.emit(result)
                }
            } else {
                collector.emit(result)
            }
        }
    }
}

private class AppResultFailureCollector<T>(
    private val upstream: Flow<AppResult<T>>,
    private val onFailure: suspend (String) -> Unit,
    private val interceptable: Boolean = false
) : Flow<AppResult<T>> {
    override suspend fun collect(collector: FlowCollector<AppResult<T>>) {
        upstream.collect { result ->
            if (result is AppResult.Failure) {
                onFailure(result.error)
                if (interceptable) {
                    collector.emit(result)
                }
            } else {
                collector.emit(result)
            }
        }
    }
}

private class AppResultLoadingCollector<T>(
    private val upstream: Flow<AppResult<T>>,
    private val onLoading: suspend () -> Unit,
    private val interceptable: Boolean = false
) : Flow<AppResult<T>> {
    override suspend fun collect(collector: FlowCollector<AppResult<T>>) {
        upstream.collect { result ->
            if (result is AppResult.Loading) {
                onLoading()
                if (interceptable) {
                    collector.emit(result)
                }
            } else {
                collector.emit(result)
            }
        }
    }
}