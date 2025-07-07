package com.techullurgy.chessk.shared.dto

import com.techullurgy.chessk.shared.models.GameRoom
import com.techullurgy.chessk.shared.models.PieceColor
import kotlinx.serialization.Serializable

@Serializable
data class GameRoomResponse(
    val room: GameRoom,
    val joinedUsers: Map<PieceColor, String>
)
