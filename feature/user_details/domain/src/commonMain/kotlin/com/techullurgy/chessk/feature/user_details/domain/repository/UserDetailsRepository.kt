package com.techullurgy.chessk.feature.user_details.domain.repository

import com.techullurgy.chessk.feature.user_details.domain.models.UploadProfilePictureProgressData
import com.techullurgy.chessk.feature.user_details.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserDetailsRepository {
    suspend fun registerUser(user: User): User?
    suspend fun loginUser(email: String, password: String): User?
    suspend fun getUser(id: String): User?

    fun uploadProfilePicture(picture: ByteArray): Flow<UploadProfilePictureProgressData>
}