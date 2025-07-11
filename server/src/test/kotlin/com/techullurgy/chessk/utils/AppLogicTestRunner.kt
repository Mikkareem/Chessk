package com.techullurgy.chessk.utils

import com.techullurgy.chessk.shared.dto.AuthRegisterRequest
import com.techullurgy.chessk.shared.dto.AuthSuccessResponse
import com.techullurgy.chessk.shared.dto.JoinRoomRequest
import com.techullurgy.chessk.shared.dto.JoinRoomResponse
import com.techullurgy.chessk.shared.endpoints.CreateRoomEndpoint
import com.techullurgy.chessk.shared.endpoints.GameWebsocketEndpoint
import com.techullurgy.chessk.shared.endpoints.JoinRoomEndpoint
import com.techullurgy.chessk.shared.endpoints.RegisterUserEndpoint
import com.techullurgy.chessk.shared.endpoints.StartGameEndpoint
import com.techullurgy.chessk.shared.events.ServerToClientBaseEvent
import com.techullurgy.chessk.shared.events.baseEventJson
import com.techullurgy.chessk.shared.models.GameRoomShared
import com.techullurgy.chessk.shared.models.PieceColorShared
import com.techullurgy.chessk.shared.utils.SharedConstants
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.json.Json
import kotlin.test.assertEquals

object AppLogicTestRunner {

    lateinit var apiClient1: HttpClient
        private set
    lateinit var apiClient2: HttpClient
        private set

    lateinit var websocketClient1: HttpClient
        private set
    lateinit var websocketClient2: HttpClient
        private set


    suspend fun ApplicationTestBuilder.registerUserAndCreateTestRoomAndJoinRoomAndListen(
        startGameDelay: Long = 0,
        onClient1ReceivingEvent: (ServerToClientBaseEvent) -> Unit,
        onClient2ReceivingEvent: (ServerToClientBaseEvent) -> Unit,
        block: suspend (DefaultClientWebSocketSession, DefaultClientWebSocketSession, roomId: String) -> Unit,
    ) {
        environment {
            config = ApplicationConfig("application.yaml")
        }

        apiClient1 = createClient { apiConfig() }
        apiClient2 = createClient { apiConfig() }

        websocketClient1 = createClient { websocketConfig() }
        websocketClient2 = createClient { websocketConfig() }

        supervisorScope {
            val deferred1 = async {
                apiClient1.post(RegisterUserEndpoint.actualUrl) {
                    setBody(AuthRegisterRequest("test1", "test1@gmail.com", "192837982137"))
                    contentType(ContentType.Application.Json)
                }.body<AuthSuccessResponse>()
            }
            val deferred2 = async {
                apiClient2.post(RegisterUserEndpoint.actualUrl) {
                    setBody(AuthRegisterRequest("test2", "test2@gmail.com", "uia98374das9d8a7"))
                    contentType(ContentType.Application.Json)
                }.body<AuthSuccessResponse>()
            }

            val clientId1 = deferred1.await().clientId
            val clientId2 = deferred2.await().clientId

            apiClient1 = apiClient1.configureClientIntercept(clientId1)
            apiClient2 = apiClient2.configureClientIntercept(clientId2)
            websocketClient1 = websocketClient1.configureClientIntercept(clientId1)
            websocketClient2 = websocketClient2.configureClientIntercept(clientId2)
        }

        val room = apiClient1.post(CreateRoomEndpoint.actualUrl) {
            contentType(ContentType.Application.Json)
            setBody(
                GameRoomShared(
                    roomId = "",
                    roomName = "Test Room Championship",
                    roomDescription = "Test Room Championship Description",
                    createdBy = ""
                )
            )
        }.body<GameRoomShared>()

        val assignedColors = supervisorScope {
            val deferred1 = async {
                apiClient1.post(JoinRoomEndpoint.actualUrl) {
                    contentType(ContentType.Application.Json)
                    setBody(JoinRoomRequest(room.roomId, PieceColorShared.White))
                }.body<JoinRoomResponse>()
            }
            delay(1000)
            val deferred2 = async {
                apiClient2.post(JoinRoomEndpoint.actualUrl) {
                    contentType(ContentType.Application.Json)
                    setBody(JoinRoomRequest(room.roomId, PieceColorShared.White))
                }.body<JoinRoomResponse>()
            }

            val resp1 = deferred1.await()
            val resp2 = deferred2.await()

            val allColorsAssigned = setOf(resp1.assignedColor, resp2.assignedColor)

            assertEquals(2, allColorsAssigned.size)

            listOf(resp1.assignedColor, resp2.assignedColor)
        }

        supervisorScope {
            val socketJob1 =
                async { websocketClient1.webSocketSession(GameWebsocketEndpoint.actualUrl) }
            val socketJob2 =
                async { websocketClient2.webSocketSession(GameWebsocketEndpoint.actualUrl) }

            val socket1 = socketJob1.await()
            val socket2 = socketJob2.await()

            launch {
                socket1.receiveAsBaseEventFlow<ServerToClientBaseEvent>()
                    .collect(onClient1ReceivingEvent)
            }.invokeOnCompletion { println("Job1 Completed: $it") }
            launch {
                socket2.receiveAsBaseEventFlow<ServerToClientBaseEvent>()
                    .collect(onClient2ReceivingEvent)
            }.invokeOnCompletion { println("Job2 Completed: $it") }

            launch {
                delay(startGameDelay)
                apiClient1.post(StartGameEndpoint(room.roomId).actualUrl)
            }

            val whiteSocket =
                if (assignedColors.first() == PieceColorShared.White) socket1 else socket2
            val blackSocket =
                if (assignedColors.first() == PieceColorShared.White) socket2 else socket1

            block(whiteSocket, blackSocket, room.roomId)

            whiteSocket.close()
            blackSocket.close()

            delay(10000)
        }
    }
}

private fun HttpClientConfig<out HttpClientEngineConfig>.apiConfig() {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            }
        )
    }
}

private fun HttpClientConfig<out HttpClientEngineConfig>.websocketConfig() {
    install(WebSockets) {
        pingIntervalMillis = 20_000
        contentConverter = KotlinxWebsocketSerializationConverter(
            Json(from = baseEventJson) {}
        )
    }
}

private fun HttpClient.configureClientIntercept(clientId: String): HttpClient = config {
    val plugin = createClientPlugin("ClientIdIntercept") {
        onRequest { request, _ ->
            request.parameter(SharedConstants.Parameters.CHESSK_CLIENT_ID_HEADER_KEY, clientId)
        }
    }
    install(plugin)
}

private inline fun <reified T> DefaultClientWebSocketSession.receiveAsBaseEventFlow(): Flow<T> =
    incoming.receiveAsFlow()
        .map<Frame, T> {
            converter!!.deserialize<T>(it)
        }
