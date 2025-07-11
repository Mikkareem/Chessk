package com.techullurgy.chessk.shared.events

import com.techullurgy.chessk.shared.models.MemberShared
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.PieceColorShared
import com.techullurgy.chessk.shared.models.PieceShared
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
    val availableMoves: List<MoveShared>?,
    val selectedIndex: Int
): ServerToClientBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_GAME_UPDATE)
data class GameUpdate(
    val roomId: String,
    @Serializable(with = BoardSerializer::class)
    val board: List<PieceShared?>,
    val currentTurn: PieceColorShared,
    @Serializable(with = MoveSerializer::class)
    val lastMove: MoveShared?,
    @Serializable(with = CutPiecesSerializer::class)
    val cutPieces: Set<PieceShared>?,
    val kingInCheckIndex: Int?,
    val gameStarted: Boolean
): ServerToClientBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_GAME_STARTED)
data class GameStarted(
    val roomId: String,
    val members: List<MemberShared>,
    val assignedColor: PieceColorShared,
): ServerToClientBaseEvent

@Serializable
@SerialName(BaseEventConstants.TYPE_RESET_SELECTION_DONE)
data class ResetSelectionDone(
    val roomId: String,
): ServerToClientBaseEvent