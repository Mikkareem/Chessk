package com.techullurgy.chessk.base

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class TakeSuccessResultTest {

    @Test
    fun testSuccessTakeAfterDelayWithLoading() = runBlocking {

        val numberFlow = flow {
            emit(AppResult.Loading)
            delay(400)
            emit(AppResult.Success(13))
        }

        val actual = numberFlow.takeSuccessResult()

        assertEquals(13, actual)
    }

    @Test
    fun testFailureTakeAfterDelayWithLoading() = runBlocking {

        val numberFlow = flow<AppResult<Int>> {
            emit(AppResult.Loading)
            delay(400)
            emit(AppResult.Failure)
        }

        val actual =
            assertFailsWith(AppResultFailureException::class) { numberFlow.takeSuccessResult() }

        assertIs<AppResultFailureException>(actual)

        Unit
    }

    @Test
    fun testSuccessTakeWithoutDelay() = runBlocking {
        val numberFlow = flow {
            emit(AppResult.Success(13))
        }

        val actual = numberFlow.takeSuccessResult()

        assertEquals(13, actual)
    }

    @Test
    fun testFailureTakeWithoutDelay() = runBlocking {

        val numberFlow = flow<AppResult<Int>> {
            emit(AppResult.Failure)
        }

        val actual =
            assertFailsWith(AppResultFailureException::class) { numberFlow.takeSuccessResult() }

        assertIs<AppResultFailureException>(actual)

        Unit
    }

    @Test
    fun stressTestSuccessTakeWithDelay() = runBlocking {
        val numberFlow: (Int) -> Flow<AppResult<Int>> = {
            flow {
                delay(5)
                emit(AppResult.Success(it))
            }
        }

        repeat(10000) {
            val actual = numberFlow(it).takeSuccessResult()
            assertEquals(it, actual)
        }
    }

    @Test
    fun stressTestFailureTakeWithDelay() = runBlocking {
        val numberFlow: (Int) -> Flow<AppResult<Int>> = {
            flow {
                delay(5)
                emit(AppResult.Failure)
            }
        }

        repeat(10000) {
            val actual =
                assertFailsWith(AppResultFailureException::class) { numberFlow(it).takeSuccessResult() }
            assertIs<AppResultFailureException>(actual)
        }
    }

    @Test
    fun stressTestSuccessTakeWithoutDelay() = runBlocking {
        val numberFlow: (Int) -> Flow<AppResult<Int>> = {
            flow {
                emit(AppResult.Loading)
                emit(AppResult.Success(it))
            }
        }

        repeat(10000) {
            val actual = numberFlow(it).takeSuccessResult()
            assertEquals(it, actual)
        }
    }

    @Test
    fun stressTestFailureTakeWithoutDelay() = runBlocking {
        val numberFlow: (Int) -> Flow<AppResult<Int>> = {
            flow {
                emit(AppResult.Loading)
                emit(AppResult.Failure)
            }
        }

        repeat(10000) {
            val actual =
                assertFailsWith(AppResultFailureException::class) { numberFlow(it).takeSuccessResult() }
            assertIs<AppResultFailureException>(actual)
        }
    }
}