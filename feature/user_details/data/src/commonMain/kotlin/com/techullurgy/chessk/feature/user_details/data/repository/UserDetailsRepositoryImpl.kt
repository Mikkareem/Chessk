package com.techullurgy.chessk.feature.user_details.data.repository

import com.techullurgy.chessk.core.utils.appResultFlow
import com.techullurgy.chessk.feature.user_details.domain.models.UploadProfilePictureProgressData
import com.techullurgy.chessk.feature.user_details.domain.models.User
import com.techullurgy.chessk.feature.user_details.domain.repository.UserDetailsRepository
import com.techullurgy.chessk.shared.dto.AuthLoginRequest
import com.techullurgy.chessk.shared.dto.AuthRegisterRequest
import com.techullurgy.chessk.shared.dto.AuthSuccessResponse
import com.techullurgy.chessk.shared.endpoints.LoginUserEndpoint
import com.techullurgy.chessk.shared.endpoints.RegisterUserEndpoint
import com.techullurgy.chessk.shared.endpoints.UploadProfilePictureEndpoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class UserDetailsRepositoryImpl(
    private val authClient: HttpClient
): UserDetailsRepository {
    override suspend fun loginUser(
        email: String,
        password: String
    ) = appResultFlow {
        val authLoginRequest = AuthLoginRequest(email, password)

        authClient.post(LoginUserEndpoint.actualUrl) {
            contentType(ContentType.Application.Json)
            setBody(authLoginRequest)
        }.body<AuthSuccessResponse>().clientId
    }

    override suspend fun registerUser(user: User) = appResultFlow {
        val authRegisterRequest = AuthRegisterRequest(
            name = user.name,
            email = user.email,
            password = user.password
        )

        authClient.post(RegisterUserEndpoint.actualUrl) {
            contentType(ContentType.Application.Json)
            setBody(authRegisterRequest)
        }.body<AuthSuccessResponse>().clientId
    }

    override fun uploadProfilePicture(picture: ByteArray): Flow<UploadProfilePictureProgressData> =
        channelFlow {
            authClient.submitFormWithBinaryData(
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
                    send(
                        UploadProfilePictureProgressData(
                            totalBytes = totalBytesLength,
                            sentBytes = bytesSentTotal
                        )
                    )
                }
            }
        }
}