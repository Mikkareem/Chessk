package com.techullurgy.chessk.feature.user_details.data.repository

import com.techullurgy.chessk.base.AppResult
import kotlinx.coroutines.flow.Flow

interface UserDetailsRepository {
    fun loginUser(email: String, password: String): Flow<AppResult<String>>
    fun registerUser(name: String, email: String, password: String): Flow<AppResult<String>>

    // TODO: Need to Change to AppResult Flow
    fun uploadProfilePicture(picture: ByteArray): Flow<Float>
}