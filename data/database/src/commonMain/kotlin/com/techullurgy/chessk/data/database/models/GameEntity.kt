package com.techullurgy.chessk.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.PieceColorShared
import com.techullurgy.chessk.shared.models.PieceShared

typealias BoardPieces = List<PieceShared?>
typealias CutPieces = Set<PieceShared>

@Entity
data class GameEntity(
    @PrimaryKey val roomId: String,
    val roomName: String,
    val roomDescription: String,
    val createdBy: String,
    val board: BoardPieces = emptyList(),
    val assignedColor: PieceColorShared? = null,
    val currentPlayer: PieceColorShared? = null,
    val cutPieces: CutPieces? = null,
    val availableMoves: List<MoveShared>? = null,
    val lastMove: MoveShared? = null,
    val selectedIndex: Int = -1,
    val kingInCheckIndex: Int? = null
)
