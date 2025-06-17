package com.techullurgy.chessk.database.models.projections

data class GameHeaderProjection(
    val roomId: String,
    val roomName: String,
    val membersCount: Int,
    val isMyTurn: Boolean,
    val yourTime: Long,
    val opponentTime: Long
)
