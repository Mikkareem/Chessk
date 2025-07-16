package com.techullurgy.chessk.feature.game.models

data class GameRoom(
    val roomId: String,
    val roomName: String,
    val roomDescription: String,
    val createdBy: String,
    val members: List<Member> = emptyList(),
    val board: List<Piece?> = emptyList(),
    val assignedColor: PieceColor? = null,
    val currentPlayer: PieceColor? = null,
    val cutPieces: Set<Piece>? = null,
    val availableMoves: List<Move>? = null,
    val lastMove: Move? = null,
    val selectedIndex: Int = -1,
    val kingInCheckIndex: Int? = null,
    val yourTime: Long = 0,
    val opponentTime: Long = 0
)
