package com.techullurgy.chessk.feature.user_details.data.repository

import com.techullurgy.chessk.base.AppResult
import com.techullurgy.chessk.base.convertTo
import com.techullurgy.chessk.data.datastore.DatastoreManager
import com.techullurgy.chessk.data.remote.RemoteDataSource
import com.techullurgy.chessk.shared.dto.AuthLoginRequest
import com.techullurgy.chessk.shared.dto.AuthRegisterRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class UserDetailsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val datastoreManager: DatastoreManager,
): UserDetailsRepository {
    override fun loginUser(
        email: String,
        password: String
    ): Flow<AppResult<String>> {
        val authLoginRequest = AuthLoginRequest(email, password)

        return remoteDataSource.loginUser(authLoginRequest)
            .onEach {
                if (it is AppResult.Success) {
                    datastoreManager.storeClientId(it.data.clientId)
                }
            }
            .map { r ->
                r.convertTo { it.clientId }
            }
    }

    override fun registerUser(
        name: String,
        email: String,
        password: String
    ): Flow<AppResult<String>> {
        val authRegisterRequest = AuthRegisterRequest(
            name = name,
            email = email,
            password = password
        )

        return remoteDataSource.registerUser(authRegisterRequest)
            .onEach {
                if (it is AppResult.Success) {
                    datastoreManager.storeClientId(it.data.clientId)
                }
            }
            .map { r ->
                r.convertTo { it.clientId }
            }
    }

    override fun uploadProfilePicture(picture: ByteArray): Flow<Float> =
        remoteDataSource.uploadProfilePicture(picture)
}