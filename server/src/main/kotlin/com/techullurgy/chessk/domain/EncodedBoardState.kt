package com.techullurgy.chessk.domain

import com.techullurgy.chessk.shared.events.GameUpdate
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.PieceColorShared
import com.techullurgy.chessk.shared.models.PieceShared

data class EncodedBoardState(
    val roomId: String,
    val board: List<PieceShared?>,
    val cutPieces: Set<PieceShared>?,
    val currentTurn: PieceColorShared,
    val kingInCheckIndex: Int?,
    val lastMove: MoveShared?,
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