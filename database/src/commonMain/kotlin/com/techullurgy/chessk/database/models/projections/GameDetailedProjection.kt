package com.techullurgy.chessk.database.models.projections

import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.database.models.BoardPieces
import com.techullurgy.chessk.database.models.CutPieces
import com.techullurgy.chessk.shared.models.PieceColor

data class GameDetailedProjection(
    val roomId: String,
    val roomName: String,
    val board: BoardPieces,
    val assignedColor: PieceColor,
    val isMyTurn: Boolean,
    val yourTime: Long,
    val opponentTime: Long,
    val cutPieces: CutPieces? = null,
    val lastMove: Move? = null,
    val availableMoves: List<Move>? = null,
    val selectedIndex: Int = -1,
    val kingInCheckIndex: Int? = null,
)
