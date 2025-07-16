package com.techullurgy.chessk.shared.dto

import com.techullurgy.chessk.shared.models.PieceColorShared
import kotlinx.serialization.Serializable

@Serializable
data class JoinRoomResponse(
    val roomId: String,
    val assignedColor: PieceColorShared,
)
