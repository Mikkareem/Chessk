package com.techullurgy.chessk.feature.user_details.data.repository

import com.techullurgy.chessk.feature.user_details.domain.models.UploadProfilePictureProgressData
import com.techullurgy.chessk.feature.user_details.domain.models.User
import com.techullurgy.chessk.feature.user_details.domain.repository.UserDetailsRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class UserDetailsRepositoryImpl(
    private val apiClient: HttpClient
): UserDetailsRepository {
    override suspend fun getUser(id: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun loginUser(
        email: String,
        password: String
    ): User? {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(user: User): User? {
        TODO("Not yet implemented")
    }

    override fun uploadProfilePicture(picture: ByteArray): Flow<UploadProfilePictureProgressData> =
        channelFlow {
            apiClient.submitFormWithBinaryData(
                url = "/user/profile/picture",
                formData = formData {
                    append("type", "profile_picture")
                    append("content", picture, Headers.build {
                        append(HttpHeaders.ContentType, "image/*")
                        append(HttpHeaders.ContentDisposition, "filename=\"profile_picture.jpg\"")
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