package com.techullurgy.chessk.navigation

import kotlinx.serialization.Serializable

@Serializable
internal data object LoginRoute

@Serializable
internal data object RegisterRoute

@Serializable
internal data object MenuRoute

@Serializable
internal data object CreateRoomRoute

@Serializable
internal data object CreatedRoomsRoute

@Serializable
internal data object JoinRoomRoute

@Serializable
internal data object JoinedGamesRoute

@Serializable
internal data class GameRoomRoute(
    val roomId: String
)