package com.techullurgy.chessk.utils

import com.techullurgy.chessk.shared.events.CellSelection
import com.techullurgy.chessk.shared.events.PieceMove
import com.techullurgy.chessk.shared.events.ResetSelection
import com.techullurgy.chessk.shared.models.Bishop
import com.techullurgy.chessk.shared.models.King
import com.techullurgy.chessk.shared.models.Knight
import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.models.Pawn
import com.techullurgy.chessk.shared.models.Piece
import com.techullurgy.chessk.shared.models.PieceColor
import com.techullurgy.chessk.shared.models.Queen
import com.techullurgy.chessk.shared.models.Rook

internal fun interleaveMoves(moves: List<Move>, roomId: String): List<TestEventData> {
    val distributed = distributeMoves(moves, roomId)
    return buildList {
        val maxSize = maxOf(distributed.first.size, distributed.second.size)
        (0 until maxSize).forEach {
            if (it < distributed.first.size) add(distributed.first[it])
            if (it < distributed.second.size) add(distributed.second[it])
        }
    }
}

internal fun distributeMoves(
    moves: List<Move>,
    roomId: String
): Pair<List<TestEventData>, List<TestEventData>> {
    var board = initialBoard
    var cutPieces = hashSetOf<Piece>()

    val events = moves
        .mapIndexed { index, m ->
            val currentPlayer = board[m.from]!!.color

            val lastMove = if (index != 0) moves[index - 1] else null

            val events = listOf(
                CellSelection(roomId, currentPlayer, m.from),
                PieceMove(roomId, currentPlayer, m.from, m.to)
            )

            board.afterMoveCutPiece(m)?.let { cutPieces.add(it) }

            TestEventData(
                events = events,
                result = TestEventDataResult(
                    board = board.afterMove(m),
                    lastMove = lastMove,
                    currentPlayer = currentPlayer,
                    cutPieces = cutPieces
                )
            ).also {
                board = board.afterMove(m)
            }
        }

    val first = events.filter {
        val k = it.events.first()
        when (k) {
            is CellSelection -> k.color == PieceColor.White
            is PieceMove -> k.color == PieceColor.White
            is ResetSelection -> TODO()
            else -> TODO()
        }
    }

    val second = events.filter {
        val k = it.events.first()
        when (k) {
            is CellSelection -> k.color == PieceColor.Black
            is PieceMove -> k.color == PieceColor.Black
            is ResetSelection -> TODO()
            else -> TODO()
        }
    }

    return first to second
}

private fun List<Piece?>.afterMove(move: Move): List<Piece?> = this.toMutableList().apply {
    this[move.to] = this[move.from]
    this[move.from] = null
}

private fun List<Piece?>.afterMoveCutPiece(move: Move): Piece? = this[move.to]

private val initialBoard = listOf(
    Rook(PieceColor.Black),
    Knight(PieceColor.Black),
    Bishop(PieceColor.Black),
    Queen(PieceColor.Black),
    King(PieceColor.Black),
    Bishop(PieceColor.Black),
    Knight(PieceColor.Black),
    Rook(PieceColor.Black)
) +
        List(8) { Pawn(PieceColor.Black) } +
        List(32) { null } +
        List(8) { Pawn(PieceColor.White) } +
        listOf(
            Rook(PieceColor.White),
            Knight(PieceColor.White),
            Bishop(PieceColor.White),
            Queen(PieceColor.White),
            King(PieceColor.White),
            Bishop(PieceColor.White),
            Knight(PieceColor.White),
            Rook(PieceColor.White)
        )
