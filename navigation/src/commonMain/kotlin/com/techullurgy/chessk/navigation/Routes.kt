package com.techullurgy.chessk.navigation

import kotlinx.serialization.Serializable

@Serializable
internal data class GameRoomRoute(
    val roomId: String
)

@Serializable
internal data object JoinedGamesRoute