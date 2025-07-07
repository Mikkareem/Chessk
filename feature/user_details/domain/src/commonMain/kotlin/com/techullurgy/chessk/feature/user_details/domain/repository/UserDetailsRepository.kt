package com.techullurgy.chessk.feature.user_details.domain.repository

import com.techullurgy.chessk.core.models.AppResult
import com.techullurgy.chessk.feature.user_details.domain.models.UploadProfilePictureProgressData
import com.techullurgy.chessk.feature.user_details.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserDetailsRepository {
    suspend fun registerUser(user: User): Flow<AppResult<String>>
    suspend fun loginUser(email: String, password: String): Flow<AppResult<String>>

    fun uploadProfilePicture(picture: ByteArray): Flow<UploadProfilePictureProgressData>
}