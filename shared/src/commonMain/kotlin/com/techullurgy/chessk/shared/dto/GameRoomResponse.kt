package com.techullurgy.chessk.shared.dto

import com.techullurgy.chessk.shared.models.GameRoomShared
import com.techullurgy.chessk.shared.models.MemberShared
import kotlinx.serialization.Serializable

@Serializable
data class GameRoomResponse(
    val room: GameRoomShared,
    val joinedUsers: List<MemberShared>
)
