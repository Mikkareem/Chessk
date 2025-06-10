package com.techullurgy.chessk.shared.events

import com.techullurgy.chessk.shared.models.Member
import com.techullurgy.chessk.shared.models.PieceColor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ServerToClientBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_TIMER_UPDATE)
data class TimerUpdate(
    val roomId: String,
    val whiteTime: Long,
    val blackTime: Long,
): ServerToClientBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_SELECTION_RESULT)
data class SelectionResult(
    val roomId: String,
    val availableIndices: List<Int>,
    val selectedIndex: Int
): ServerToClientBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_GAME_UPDATE)
data class GameUpdate(
    val board: String,
    val currentTurn: PieceColor,
    val lastMove: String?,
    val cutPieces: String?,
    val kingInCheckIndex: Int?,
    val gameOver: Boolean
): ServerToClientBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_GAME_STARTED)
data class GameStarted(
    val roomId: String,
    val members: List<Member>,
    val assignedColor: PieceColor? = null,
): ServerToClientBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_RESET_SELECTION_DONE)
data class ResetSelectionDone(
    val roomId: String,
): ServerToClientBaseEvent