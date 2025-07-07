package com.techullurgy.chessk

import com.techullurgy.chessk.shared.events.ClientToServerBaseEvent
import com.techullurgy.chessk.shared.events.EnterRoomHandshake
import com.techullurgy.chessk.shared.events.ServerToClientBaseEvent
import com.techullurgy.chessk.shared.events.baseEventJson
import com.techullurgy.chessk.shared.models.GameRoom
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
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

    //    @Test
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
                        GameRoom.serializer(),
                        GameRoom(
                            roomId = "",
                            roomName = "Test Room Championship",
                            roomDescription = "Test Room Championship Description",
                            createdBy = "tester"
                        )
                    )
                    setBody(roomModel)
                }

                response.bodyAsText()
            }

            val roomId = Json.decodeFromString<GameRoom>(deferred.await()).roomId

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

    @Test
    fun testWebsocketConnection2() = testApplication {
        environment {
            config = ApplicationConfig("application.yaml")
        }
        val client1 = createClient {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(
                    Json(from = baseEventJson) {}
                )
            }
        }
        val socket = client1.webSocketSession("/join/ws/test")

        socket.launch {
            socket.incoming.receiveAsFlow()
                .map { socket.converter!!.deserialize<ServerToClientBaseEvent>(it) }
                .collect {
                    println("Received: $it")
                }
        }

        coroutineScope {
            socket.sendSerialized<ClientToServerBaseEvent>(EnterRoomHandshake("812736"))
            socket.sendSerialized<ClientToServerBaseEvent>(EnterRoomHandshake("893847"))
            socket.sendSerialized<ClientToServerBaseEvent>(EnterRoomHandshake("676354"))
        }

        delay(3000)
    }
}