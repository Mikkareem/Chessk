package com.techullurgy.chessk.data.api.impl

import com.techullurgy.chessk.base.AppResult
import com.techullurgy.chessk.data.api.ChessKApi
import com.techullurgy.chessk.data.api.utils.postWithBody
import com.techullurgy.chessk.data.api.utils.postWithoutBody
import com.techullurgy.chessk.data.api.utils.safeNetworkCall
import com.techullurgy.chessk.shared.dto.AuthLoginRequest
import com.techullurgy.chessk.shared.dto.AuthRegisterRequest
import com.techullurgy.chessk.shared.dto.AuthSuccessResponse
import com.techullurgy.chessk.shared.dto.CreateRoomRequest
import com.techullurgy.chessk.shared.dto.CreateRoomResponse
import com.techullurgy.chessk.shared.dto.GameRoomResponse
import com.techullurgy.chessk.shared.dto.JoinRoomRequest
import com.techullurgy.chessk.shared.dto.JoinRoomResponse
import com.techullurgy.chessk.shared.endpoints.CreateRoomEndpoint
import com.techullurgy.chessk.shared.endpoints.GetCreatedRoomsEndpoint
import com.techullurgy.chessk.shared.endpoints.GetJoinedRoomsEndpoint
import com.techullurgy.chessk.shared.endpoints.JoinRoomEndpoint
import com.techullurgy.chessk.shared.endpoints.LeaveRoomEndpoint
import com.techullurgy.chessk.shared.endpoints.LoginUserEndpoint
import com.techullurgy.chessk.shared.endpoints.RegisterUserEndpoint
import com.techullurgy.chessk.shared.endpoints.StartGameEndpoint
import com.techullurgy.chessk.shared.endpoints.UploadProfilePictureEndpoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.http.ContentDisposition
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

internal class KtorChessKApi(
    private val httpClient: HttpClient,
    val wsClient: HttpClient,
    private val noAuthHttpClient: HttpClient
) : ChessKApi {
    override fun registerUser(body: AuthRegisterRequest): Flow<AppResult<AuthSuccessResponse>> =
        safeNetworkCall {
            val response = noAuthHttpClient
                .postWithBody(
                    url = RegisterUserEndpoint.actualUrl,
                    body = body
                )

            response.body<AuthSuccessResponse>()
        }

    override fun loginUser(body: AuthLoginRequest): Flow<AppResult<AuthSuccessResponse>> =
        safeNetworkCall {
            val response = noAuthHttpClient
                .postWithBody(
                    url = LoginUserEndpoint.actualUrl,
                    body = body
                )

            response.body<AuthSuccessResponse>()
        }

    override fun createRoom(body: CreateRoomRequest) = safeNetworkCall {
        val response = httpClient
            .postWithBody(
                url = CreateRoomEndpoint.actualUrl,
                body = body
            )

        response.body<CreateRoomResponse>()
    }

    override fun joinRoom(body: JoinRoomRequest) = safeNetworkCall {
        val response = httpClient
            .postWithBody(
                url = JoinRoomEndpoint.actualUrl,
                body = body
            )

        response.body<JoinRoomResponse>()
    }

    override fun getCreatedRooms() = safeNetworkCall {
        val response = httpClient.get(GetCreatedRoomsEndpoint.actualUrl)
        response.body<List<GameRoomResponse>>()
    }

    override fun getJoinedRooms() = safeNetworkCall {
        val response = httpClient.get(GetJoinedRoomsEndpoint.actualUrl)
        response.body<List<GameRoomResponse>>()
    }

    override fun startGame(roomId: String) = safeNetworkCall {
        val response = httpClient.postWithoutBody(url = StartGameEndpoint(roomId).actualUrl)
        response.body<Unit>()
    }

    override fun leaveRoom(roomId: String) = safeNetworkCall {
        val response = httpClient.postWithoutBody(url = LeaveRoomEndpoint(roomId).actualUrl)
        response.body<Unit>()
    }

    override fun uploadProfilePicture(bytes: ByteArray): Flow<Float> =
        uploadProfilePicture(bytes, httpClient)
}

private fun uploadProfilePicture(picture: ByteArray, client: HttpClient): Flow<Float> =
    channelFlow {
        client.submitFormWithBinaryData(
            url = UploadProfilePictureEndpoint.actualUrl,
            formData = formData {
                append("type", "profile_picture")
                append("content", picture, Headers.build {
                    append(HttpHeaders.ContentType, "image/*")
                    append(
                        HttpHeaders.ContentDisposition,
                        ContentDisposition.Attachment.withParameter(
                            ContentDisposition.Parameters.FileName,
                            "profile_picture.jpg"
                        ).toString()
                    )
                })
            }
        ) {
            onUpload { bytesSentTotal, totalBytesLength ->
                val totalBytesLengthAdjusted = totalBytesLength ?: 1L
                if (totalBytesLengthAdjusted == 1L) {
                    send(1f)
                } else {
                    val percent = (bytesSentTotal / totalBytesLengthAdjusted).toFloat()
                    send(percent)
                }
            }
        }
    }