package com.techullurgy.chessk

import com.techullurgy.chessk.domain.RoomModel
import com.techullurgy.chessk.shared.events.ClientToServerBaseEvent
import com.techullurgy.chessk.shared.events.EnterRoomHandshake
import io.ktor.client.plugins.api.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test


fun interceptor(clientId: String) = createClientPlugin("Intercept") {
    onRequest { request, _ ->
        request.parameter("client_id", clientId)
    }
}

class WebsocketTesting {

    @Test
    fun testWebSocketsConnection() = testApplication {
        application {
            module()
        }

        val client1 = createClient {
            install(interceptor("client1"))
            install(HttpCookies)
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        }

        val client2 = createClient {
            install(interceptor("client2"))
            install(HttpCookies)
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        }

        runBlocking {
            val deferred = async {
                val response = client1.post("/room/create") {
                    contentType(ContentType.Application.Json)
                    val roomModel = Json.encodeToString(
                        RoomModel.serializer(),
                        RoomModel(
                            id = "",
                            name = "Test Room Championship",
                            description = "Test Room Championship Description",
                            createdBy = "tester"
                        )
                    )
                    setBody(roomModel)
                }

                response.bodyAsText()
            }

            val roomId = Json.decodeFromString<RoomModel>(deferred.await()).id

            launch {
//                client1.post("/room/$roomId/join") {
//                    contentType(ContentType.Application.Json)
//                    setBody(
//                        Json.encodeToString(
//                            RoomJoinRequest.serializer(),
//                            RoomJoinRequest("irsath", color = Color.Black)
//                        )
//                    )
//                }
//                client2.post("/room/$roomId/join") {
//                    contentType(ContentType.Application.Json)
//                    setBody(
//                        Json.encodeToString(
//                            RoomJoinRequest.serializer(),
//                            RoomJoinRequest("kareem", color = Color.Black)
//                        )
//                    )
//                }
//                client1.post("/room/$roomId/start") {  }
            }

            delay(5000)

            launch {
                client1.webSocket("/join/ws") {
                    sendSerialized<ClientToServerBaseEvent>(EnterRoomHandshake(roomId))
                    for (frame in incoming) {
                        if(frame is Frame.Text) {
                            println("Received for irsath: ${frame.readText()}")
                        }
                    }
                }
            }.invokeOnCompletion { cause -> println("Irsath Coroutine completed") }

            launch {
                client2.webSocket("/join/ws") {
                    sendSerialized<ClientToServerBaseEvent>(EnterRoomHandshake(roomId))
                    for (frame in incoming) {
                        if(frame is Frame.Text) {
                            println("Received for kareem: ${frame.readText()}")
                        }
                    }
                }
            }.invokeOnCompletion { cause -> println("Kareem Coroutine completed") }
        }
    }
}