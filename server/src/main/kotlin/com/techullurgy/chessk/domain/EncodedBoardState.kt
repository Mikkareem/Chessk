package com.techullurgy.chessk.domain

import com.techullurgy.chessk.shared.events.GameUpdate
import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.models.Piece
import com.techullurgy.chessk.shared.models.PieceColor

data class EncodedBoardState(
    val roomId: String,
    val board: List<Piece?>,
    val cutPieces: Set<Piece>?,
    val currentTurn: PieceColor,
    val kingInCheckIndex: Int?,
    val lastMove: Move?,
    val gameStarted: Boolean
)

fun EncodedBoardState.toGameUpdate() = GameUpdate(
    roomId = roomId,
    board = board,
    currentTurn = currentTurn,
    lastMove = lastMove,
    cutPieces = cutPieces,
    kingInCheckIndex = kingInCheckIndex,
    gameStarted = gameStarted
)