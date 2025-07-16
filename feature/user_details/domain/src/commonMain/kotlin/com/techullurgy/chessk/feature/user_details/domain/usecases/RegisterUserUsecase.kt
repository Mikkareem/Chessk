package com.techullurgy.chessk.feature.user_details.domain.usecases

import com.techullurgy.chessk.core.models.AppResult
import com.techullurgy.chessk.core.utils.transformAppResult
import com.techullurgy.chessk.feature.user_details.domain.models.User
import com.techullurgy.chessk.feature.user_details.domain.repository.UserDetailsRepository
import kotlinx.coroutines.flow.Flow

class RegisterUserUsecase(
    private val repository: UserDetailsRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Flow<AppResult<Boolean>> {
        val user = User(name, email, password)

        return repository.registerUser(user)
            .transformAppResult {
                // Store the client id in Datastore
                AppResult.Success(true)
            }
    }
}