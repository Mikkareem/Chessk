package com.techullurgy.chessk.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.models.Piece
import com.techullurgy.chessk.shared.models.PieceColor

typealias BoardPieces = List<Piece?>
typealias CutPieces = Set<Piece>

@Entity
data class GameEntity(
    @PrimaryKey val roomId: String,
    val board: BoardPieces = emptyList(),
    val assignedColor: PieceColor? = null,
    val currentPlayer: PieceColor? = null,
    val cutPieces: CutPieces? = null,
    val availableMoves: List<Move>? = null,
    val lastMove: Move? = null,
    val selectedIndex: Int = -1,
    val kingInCheckIndex: Int? = null
)
