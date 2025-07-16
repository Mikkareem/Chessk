package com.techullurgy.chessk.feature.user_details.domain.models

data class User(
    val name: String,
    val email: String,
    val password: String,
    val profilePictureUrl: String? = null
)