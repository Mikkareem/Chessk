package com.techullurgy.chessk.utils

import com.techullurgy.chessk.shared.events.CellSelection
import com.techullurgy.chessk.shared.events.ClientToServerBaseEvent
import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.models.Piece
import com.techullurgy.chessk.shared.models.PieceColor

data class TestEventDataResult(
    val board: List<Piece?>,
    val lastMove: Move? = null,
    val currentPlayer: PieceColor? = null,
    val cutPieces: Set<Piece> = emptySet()
) {
    override fun toString(): String {
        val boardString = board.chunked(8).joinToString("\n|        ") {
            it.joinToString(" ") {
                when (it) {
                    null -> "."
                    else -> it.encodeAsString()
                }
            }
        }
        val f = """
            |
            |TestEventDataResult(
            |   board=[
            |       $boardString
            |   ],
            |   lastMove=$lastMove,
            |   currentPlayer=$currentPlayer,
            |   cutPieces=$cutPieces
            |)""".trimMargin()
        return f
    }
}

data class TestEventData(
    val events: List<ClientToServerBaseEvent>,
    val result: TestEventDataResult
) {
    fun getOwner(): PieceColor = (events.first() as CellSelection).color
}
