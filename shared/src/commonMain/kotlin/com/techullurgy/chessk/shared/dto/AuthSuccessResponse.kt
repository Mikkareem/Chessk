package com.techullurgy.chessk.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthSuccessResponse(
    val clientId: String
)
