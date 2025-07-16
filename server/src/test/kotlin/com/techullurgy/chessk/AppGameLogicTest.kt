package com.techullurgy.chessk

import com.techullurgy.chessk.shared.events.ClientToServerBaseEvent
import com.techullurgy.chessk.shared.events.ServerToClientBaseEvent
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.PieceColorShared
import com.techullurgy.chessk.utils.AppLogicTestRunner
import com.techullurgy.chessk.utils.interleaveMoves
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.test.Test

class AppGameLogicTest {

    private val client1Outputs = mutableListOf<ServerToClientBaseEvent>()
    private val client2Outputs = mutableListOf<ServerToClientBaseEvent>()

    @Test
    fun `test messages passing correctly`() = testApplication {
        with(AppLogicTestRunner) {
            registerUserAndCreateTestRoomAndJoinRoomAndListen(
                onClient1ReceivingEvent = {
                    println("Received for 1: $it")
                },
                onClient2ReceivingEvent = {
                    println("Received for 2: $it")
                }
            ) { whiteSocket, blackSocket, roomId ->
                val moves = listOf(
                    MoveShared(49, 33),
                    MoveShared(14, 30),
                    MoveShared(58, 40),
                    MoveShared(6, 21),
                    MoveShared(62, 45),
                    MoveShared(21, 27),
                )

                val interleaved = interleaveMoves(moves, roomId)

                interleaved.forEach { eventData ->
                    when (eventData.getOwner()) {
                        PieceColorShared.Black -> {
                            eventData.events.forEach {
                                blackSocket.sendWithRandomDelay(it) {
                                    println("Sending from 2: $it")
                                }
                            }
                        }

                        PieceColorShared.White -> {
                            eventData.events.forEach {
                                whiteSocket.sendWithRandomDelay(it) {
                                    println("Sending from 1: $it")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.sendWithRandomDelay(
        event: ClientToServerBaseEvent,
        block: (() -> Unit)? = null
    ) {
        delay(Random.nextLong(from = 2000, until = 6000))
        block?.invoke()
        sendSerialized(event)
        delay(700)
    }
}