package com.techullurgy.chessk.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val name: String,
    val assignedColor: PieceColor,
    val userId: String,
    val profilePicUrl: String,
)