package com.techullurgy.chessk.shared.dto

import com.techullurgy.chessk.shared.models.PieceColorShared
import kotlinx.serialization.Serializable

@Serializable
data class JoinRoomRequest(
    val roomId: String,
    val preferredColor: PieceColorShared
)
