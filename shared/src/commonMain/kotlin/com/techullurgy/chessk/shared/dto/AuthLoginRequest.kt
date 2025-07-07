package com.techullurgy.chessk.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginRequest(
    val email: String,
    val password: String,
)
