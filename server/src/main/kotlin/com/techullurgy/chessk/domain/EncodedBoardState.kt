package com.techullurgy.chessk.domain

import com.techullurgy.chessk.shared.events.GameUpdate
import com.techullurgy.chessk.shared.models.PieceColor

data class EncodedBoardState(
    val board: String,
    val cutPieces: String,
    val currentTurn: PieceColor,
    val kingInCheckIndex: Int?,
    val gameOver: Boolean,
    val lastMove: String?,
)

fun EncodedBoardState.toGameUpdate() = GameUpdate(
    board = board,
    currentTurn = currentTurn,
    lastMove = lastMove,
    cutPieces = cutPieces,
    kingInCheckIndex = kingInCheckIndex,
    gameOver = gameOver
)