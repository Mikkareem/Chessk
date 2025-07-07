package com.techullurgy.chessk.shared.events

import com.techullurgy.chessk.shared.models.Member
import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.models.Piece
import com.techullurgy.chessk.shared.models.PieceColor
import com.techullurgy.chessk.shared.utils.BoardSerializer
import com.techullurgy.chessk.shared.utils.CutPiecesSerializer
import com.techullurgy.chessk.shared.utils.MoveSerializer
import com.techullurgy.chessk.shared.utils.MovesSerializer
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
    @Serializable(with = MovesSerializer::class)
    val availableMoves: List<Move>?,
    val selectedIndex: Int
): ServerToClientBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_GAME_UPDATE)
data class GameUpdate(
    val roomId: String,
    @Serializable(with = BoardSerializer::class)
    val board: List<Piece?>,
    val currentTurn: PieceColor,
    @Serializable(with = MoveSerializer::class)
    val lastMove: Move?,
    @Serializable(with = CutPiecesSerializer::class)
    val cutPieces: Set<Piece>?,
    val kingInCheckIndex: Int?,
    val gameStarted: Boolean
): ServerToClientBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_GAME_STARTED)
data class GameStarted(
    val roomId: String,
    val members: List<Member>,
    val assignedColor: PieceColor,
): ServerToClientBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_RESET_SELECTION_DONE)
data class ResetSelectionDone(
    val roomId: String,
): ServerToClientBaseEvent