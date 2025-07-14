package com.techullurgy.chessk.feature.login.domain.usecases

import com.techullurgy.chessk.base.AppResult
import com.techullurgy.chessk.base.convertTo
import com.techullurgy.chessk.feature.user_details.data.repository.UserDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginUserUsecase(
    private val userDetailsRepository: UserDetailsRepository
) {
    operator fun invoke(
        email: String,
        password: String
    ): Flow<AppResult<Unit>> = userDetailsRepository.loginUser(email, password)
        .map { it.convertTo { } }
}