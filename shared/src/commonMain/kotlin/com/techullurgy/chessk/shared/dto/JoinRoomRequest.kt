package com.techullurgy.chessk.shared.dto

import com.techullurgy.chessk.shared.models.PieceColor
import kotlinx.serialization.Serializable

@Serializable
data class JoinRoomRequest(
    val roomId: String,
    val preferredColor: PieceColor
)
