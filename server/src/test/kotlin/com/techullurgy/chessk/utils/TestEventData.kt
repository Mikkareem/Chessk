package com.techullurgy.chessk.utils

import com.techullurgy.chessk.shared.events.CellSelection
import com.techullurgy.chessk.shared.events.ClientToServerBaseEvent
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.PieceColorShared
import com.techullurgy.chessk.shared.models.PieceShared

data class TestEventDataResult(
    val board: List<PieceShared?>,
    val lastMove: MoveShared? = null,
    val currentPlayer: PieceColorShared? = null,
    val cutPieces: Set<PieceShared> = emptySet()
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
    fun getOwner(): PieceColorShared = (events.first() as CellSelection).color
}
