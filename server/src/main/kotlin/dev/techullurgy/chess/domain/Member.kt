package dev.techullurgy.chess.domain

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val name: String,
    val assignedColor: Color,
    val userId: String,
    val profilePicUrl: String,
)
