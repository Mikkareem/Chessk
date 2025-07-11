package com.techullurgy.chessk.base

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.atomics.ExperimentalAtomicApi

sealed interface AppResult<out T> {
    data object Loading : AppResult<Nothing>
    data class Success<T>(val data: T) : AppResult<T>
    data object Failure : AppResult<Nothing>

    data class FailureWithException(val exc: Throwable) : AppResult<Nothing>
}

fun <T, R> AppResult<T>.convertTo(block: (T) -> R): AppResult<R> = when (this) {
    AppResult.Failure -> AppResult.Failure
    AppResult.Loading -> AppResult.Loading
    is AppResult.Success -> AppResult.Success(block(data))
    is AppResult.FailureWithException -> AppResult.FailureWithException(exc)
}

@OptIn(ExperimentalAtomicApi::class)
suspend fun <T> Flow<AppResult<T>>.takeSuccessResult(): T {
    val mutex = Mutex()
    return supervisorScope {
        async {
            var answer: T? = null
            launch {
                collect {
                    when (it) {
                        is AppResult.FailureWithException -> coroutineContext[Job]?.cancel()
                        AppResult.Failure -> coroutineContext[Job]?.cancel()
                        AppResult.Loading -> {}
                        is AppResult.Success -> {
                            mutex.withLock {
                                answer = it.data
                            }
                            coroutineContext[Job]?.cancel()
                        }
                    }
                }
            }.join()
            answer ?: throw AppResultFailureException()
        }.await()
    }
}

class AppResultFailureException : RuntimeException()