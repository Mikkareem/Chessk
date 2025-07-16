package com.techullurgy.chessk.shared.dto

import com.techullurgy.chessk.shared.models.GameRoomShared

data class CreateRoomRequest(
    val room: GameRoomShared
)
