package com.techullurgy.chessk.feature.game.models

data class Member(
    val roomId: String,
    val name: String,
    val assignedColor: PieceColor,
    val userId: String,
    val profilePicUrl: String?,
    val isOwner: Boolean
)