package com.techullurgy.chessk.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class MemberShared(
    val roomId: String,
    val name: String,
    val assignedColor: PieceColorShared,
    val userId: String,
    val profilePicUrl: String?,
    val isOwner: Boolean
)