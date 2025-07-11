package com.techullurgy.chessk.shared.events

import com.techullurgy.chessk.shared.models.PieceColorShared
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ClientToServerBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_ENTER_ROOM_HANDSHAKE)
data class EnterRoomHandshake(
    val roomId: String,
): ClientToServerBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_DISCONNECT)
data class Disconnected(
    val roomId: String,
): ClientToServerBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_PIECE_MOVE)
data class PieceMove(
    val roomId: String,
    val color: PieceColorShared,
    val from: Int,
    val to: Int
): ClientToServerBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_CELL_SELECTION)
data class CellSelection(
    val roomId: String,
    val color: PieceColorShared,
    val selectedIndex: Int
): ClientToServerBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_RESET_SELECTION)
data class ResetSelection(
    val roomId: String,
): ClientToServerBaseEvent