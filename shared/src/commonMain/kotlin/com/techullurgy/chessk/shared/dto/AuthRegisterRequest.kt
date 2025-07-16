package com.techullurgy.chessk.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthRegisterRequest(
    val name: String,
    val email: String,
    val password: String,
)