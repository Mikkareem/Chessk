package com.techullurgy.chessk.feature.splash.domain.usecases

import com.techullurgy.chessk.feature.user_details.data.repository.UserDetailsRepository

class CheckLoggedInUsecase(
    private val userDetailsRepository: UserDetailsRepository
) {
    operator fun invoke() = userDetailsRepository.isUserLoggedIn
}