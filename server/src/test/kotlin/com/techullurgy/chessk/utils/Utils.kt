package com.techullurgy.chessk.utils

import com.techullurgy.chessk.shared.events.CellSelection
import com.techullurgy.chessk.shared.events.PieceMove
import com.techullurgy.chessk.shared.events.ResetSelection
import com.techullurgy.chessk.shared.models.Bishop
import com.techullurgy.chessk.shared.models.King
import com.techullurgy.chessk.shared.models.Knight
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.Pawn
import com.techullurgy.chessk.shared.models.PieceColorShared
import com.techullurgy.chessk.shared.models.PieceShared
import com.techullurgy.chessk.shared.models.Queen
import com.techullurgy.chessk.shared.models.Rook

internal fun interleaveMoves(moves: List<MoveShared>, roomId: String): List<TestEventData> {
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
    moves: List<MoveShared>,
    roomId: String
): Pair<List<TestEventData>, List<TestEventData>> {
    var board = initialBoard
    var cutPieces = hashSetOf<PieceShared>()

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
            is CellSelection -> k.color == PieceColorShared.White
            is PieceMove -> k.color == PieceColorShared.White
            is ResetSelection -> TODO()
            else -> TODO()
        }
    }

    val second = events.filter {
        val k = it.events.first()
        when (k) {
            is CellSelection -> k.color == PieceColorShared.Black
            is PieceMove -> k.color == PieceColorShared.Black
            is ResetSelection -> TODO()
            else -> TODO()
        }
    }

    return first to second
}

private fun List<PieceShared?>.afterMove(move: MoveShared): List<PieceShared?> =
    this.toMutableList().apply {
    this[move.to] = this[move.from]
    this[move.from] = null
}

private fun List<PieceShared?>.afterMoveCutPiece(move: MoveShared): PieceShared? = this[move.to]

private val initialBoard = listOf(
    Rook(PieceColorShared.Black),
    Knight(PieceColorShared.Black),
    Bishop(PieceColorShared.Black),
    Queen(PieceColorShared.Black),
    King(PieceColorShared.Black),
    Bishop(PieceColorShared.Black),
    Knight(PieceColorShared.Black),
    Rook(PieceColorShared.Black)
) +
        List(8) { Pawn(PieceColorShared.Black) } +
        List(32) { null } +
        List(8) { Pawn(PieceColorShared.White) } +
        listOf(
            Rook(PieceColorShared.White),
            Knight(PieceColorShared.White),
            Bishop(PieceColorShared.White),
            Queen(PieceColorShared.White),
            King(PieceColorShared.White),
            Bishop(PieceColorShared.White),
            Knight(PieceColorShared.White),
            Rook(PieceColorShared.White)
        )
