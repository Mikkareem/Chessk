package dev.techullurgy.chess.domain

import kotlinx.serialization.Serializable

@Serializable
data class RoomModel(
    val id: String,
    val name: String,
    val description: String,
    val createdBy: String
)

internal fun RoomModel.toRoom() = Room(
    id = id,
    name = name,
    description = description,
    createdBy = createdBy
)

internal fun Room.toRoomModel() = RoomModel(
    id = id,
    name = name,
    description = description,
    createdBy = createdBy
)