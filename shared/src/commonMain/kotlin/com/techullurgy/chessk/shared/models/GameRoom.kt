package com.techullurgy.chessk.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class GameRoom(
    val roomId: String,
    val roomName: String,
    val roomDescription: String,
    val createdBy: String
)