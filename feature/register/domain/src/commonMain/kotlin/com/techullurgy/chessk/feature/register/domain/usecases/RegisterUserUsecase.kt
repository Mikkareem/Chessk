package com.techullurgy.chessk.feature.register.domain.usecases

import com.techullurgy.chessk.base.AppResult
import com.techullurgy.chessk.base.convertTo
import com.techullurgy.chessk.feature.user_details.data.repository.UserDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RegisterUserUsecase(
    private val userDetailsRepository: UserDetailsRepository
) {
    operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Flow<AppResult<Unit>> = userDetailsRepository.registerUser(name, email, password)
        .map { it.convertTo { } }
}