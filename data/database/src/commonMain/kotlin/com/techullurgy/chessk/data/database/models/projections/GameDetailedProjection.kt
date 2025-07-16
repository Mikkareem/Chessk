package com.techullurgy.chessk.data.database.models.projections

import com.techullurgy.chessk.data.database.models.BoardPieces
import com.techullurgy.chessk.data.database.models.CutPieces
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.PieceColorShared

data class GameDetailedProjection(
    val roomId: String,
    val roomName: String,
    val board: BoardPieces,
    val assignedColor: PieceColorShared,
    val isMyTurn: Boolean,
    val yourTime: Long,
    val opponentTime: Long,
    val cutPieces: CutPieces? = null,
    val lastMove: MoveShared? = null,
    val availableMoves: List<MoveShared>? = null,
    val selectedIndex: Int = -1,
    val kingInCheckIndex: Int? = null,
)
