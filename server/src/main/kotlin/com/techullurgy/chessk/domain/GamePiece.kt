package com.techullurgy.chessk.domain

import com.techullurgy.chessk.shared.models.Bishop
import com.techullurgy.chessk.shared.models.King
import com.techullurgy.chessk.shared.models.Knight
import com.techullurgy.chessk.shared.models.Pawn
import com.techullurgy.chessk.shared.models.Piece
import com.techullurgy.chessk.shared.models.PieceColor
import com.techullurgy.chessk.shared.models.Queen
import com.techullurgy.chessk.shared.models.Rook

sealed class GamePiece(
    val pieceColor: PieceColor,
    private val pieceIndex: Int
) {
    protected abstract fun getAvailableMovesAt(board: List<GamePiece?>): List<Int>

    protected fun getVerticalMoves(index: Int, board: List<GamePiece?>): List<Int> {
        val indices = mutableListOf<Int>()
        val currentRow = index.row
        val currentColumn = index.column

        var tempRow = currentRow + 1
        while((tempRow in 0..7)) {
            if(board.canPlace(tempRow, currentColumn, pieceColor)) {
                indices.add(tempRow rowAndColumn currentColumn)
                if(!board.isEmptyCell(tempRow, currentColumn)) break
            } else {
                break
            }
            tempRow++
        }
        tempRow = currentRow - 1
        while((tempRow in 0..7)) {
            if(board.canPlace(tempRow, currentColumn, pieceColor)) {
                indices.add(tempRow rowAndColumn currentColumn)
                if(!board.isEmptyCell(tempRow, currentColumn)) break
            } else {
                break
            }
            tempRow--
        }
        return indices
    }

    protected fun getHorizontalMoves(index: Int, board: List<GamePiece?>): List<Int> {
        val indices = mutableListOf<Int>()
        val currentRow = index.row
        val currentColumn = index.column

        var tempColumn = currentColumn + 1
        while((tempColumn in 0..7)) {
            if(board.canPlace(currentRow, tempColumn, pieceColor)) {
                indices.add(currentRow rowAndColumn tempColumn)
                if(!board.isEmptyCell(currentRow, tempColumn)) break
            } else {
                break
            }
            tempColumn++
        }
        tempColumn = currentColumn - 1
        while((tempColumn in 0..7)) {
            if(board.canPlace(currentRow, tempColumn, pieceColor)) {
                indices.add(currentRow rowAndColumn tempColumn)
                if(!board.isEmptyCell(currentRow, tempColumn)) break
            } else {
                break
            }
            tempColumn--
        }
        return indices
    }

    protected fun getDiagonalMoves(index: Int, board: List<GamePiece?>): List<Int> {
        val indices = mutableListOf<Int>()

        val currentRow = index.row
        val currentColumn = index.column

        var tempRow = currentRow-1
        var tempColumn = currentColumn+1

        while(tempRow in 0..7 && tempColumn in 0..7) {
            if(board.canPlace(tempRow, tempColumn, pieceColor)) {
                indices.add(tempRow rowAndColumn tempColumn)
                if(!board.isEmptyCell(tempRow, tempColumn)) break
            } else {
                break
            }
            tempRow--
            tempColumn++
        }

        tempRow = currentRow+1
        tempColumn = currentColumn-1

        while(tempRow in 0..7 && tempColumn in 0..7) {
            if(board.canPlace(tempRow, tempColumn, pieceColor)) {
                indices.add(tempRow rowAndColumn tempColumn)
                if(!board.isEmptyCell(tempRow, tempColumn)) break
            } else {
                break
            }
            tempRow++
            tempColumn--
        }

        return indices
    }

    protected fun getAntiDiagonalMoves(index: Int, board: List<GamePiece?>): List<Int> {
        val indices = mutableListOf<Int>()

        val currentRow = index.row
        val currentColumn = index.column

        var tempRow = currentRow-1
        var tempColumn = currentColumn-1

        while(tempRow in 0..7 && tempColumn in 0..7) {
            if(board.canPlace(tempRow, tempColumn, pieceColor)) {
                indices.add(tempRow rowAndColumn tempColumn)
                if(!board.isEmptyCell(tempRow, tempColumn)) break
            } else {
                break
            }
            tempRow--
            tempColumn--
        }

        tempRow = currentRow+1
        tempColumn = currentColumn+1

        while(tempRow in 0..7 && tempColumn in 0..7) {
            if(board.canPlace(tempRow, tempColumn, pieceColor)) {
                indices.add(tempRow rowAndColumn tempColumn)
                if(!board.isEmptyCell(tempRow, tempColumn)) break
            } else {
                break
            }
            tempRow++
            tempColumn++
        }

        return indices
    }

    protected fun getLMoves(index: Int, board: List<GamePiece?>): List<Int> {
        val indices = mutableListOf<Int>()

        val currentRow = index.row
        val currentColumn = index.column

        var tempRow = currentRow-2
        var tempColumn = currentColumn-1
        if(tempRow in 0..7 && tempColumn in 0..7 && board.canPlace(tempRow, tempColumn, pieceColor)) {
            indices.add(tempRow rowAndColumn tempColumn)
        }

        tempRow = currentRow-2
        tempColumn = currentColumn+1
        if(tempRow in 0..7 && tempColumn in 0..7 && board.canPlace(tempRow, tempColumn, pieceColor)) {
            indices.add(tempRow rowAndColumn tempColumn)
        }

        tempRow = currentRow+2
        tempColumn = currentColumn-1
        if(tempRow in 0..7 && tempColumn in 0..7 && board.canPlace(tempRow, tempColumn, pieceColor)) {
            indices.add(tempRow rowAndColumn tempColumn)
        }

        tempRow = currentRow+2
        tempColumn = currentColumn+1
        if(tempRow in 0..7 && tempColumn in 0..7 && board.canPlace(tempRow, tempColumn, pieceColor)) {
            indices.add(tempRow rowAndColumn tempColumn)
        }

        tempRow = currentRow-1
        tempColumn = currentColumn-2
        if(tempRow in 0..7 && tempColumn in 0..7 && board.canPlace(tempRow, tempColumn, pieceColor)) {
            indices.add(tempRow rowAndColumn tempColumn)
        }

        tempRow = currentRow-1
        tempColumn = currentColumn+2
        if(tempRow in 0..7 && tempColumn in 0..7 && board.canPlace(tempRow, tempColumn, pieceColor)) {
            indices.add(tempRow rowAndColumn tempColumn)
        }

        tempRow = currentRow+1
        tempColumn = currentColumn-2
        if(tempRow in 0..7 && tempColumn in 0..7 && board.canPlace(tempRow, tempColumn, pieceColor)) {
            indices.add(tempRow rowAndColumn tempColumn)
        }

        tempRow = currentRow+1
        tempColumn = currentColumn+2
        if(tempRow in 0..7 && tempColumn in 0..7 && board.canPlace(tempRow, tempColumn, pieceColor)) {
            indices.add(tempRow rowAndColumn tempColumn)
        }

        return indices
    }

    fun getAvailableIndices(board: List<GamePiece?>): List<Int> {
        return getAvailableMovesAt(board).filter {
            val piece = this
            val newBoard = board.toMutableList().apply {
                this[pieceIndex] = null
                this[it] = changeIndex(piece, it)
            }
            !isKingInCheck(newBoard)
        }
    }

    private fun isKingInCheck(board: List<GamePiece?>): Boolean {
        val gameKingPosition =
            board.filterIsInstance<GameKing>().first { it.color == pieceColor }.index
        board.filterNotNull().filter { it.pieceColor != pieceColor }.forEach {
            if (it.getAvailableMovesAt(board).contains(gameKingPosition)) return true
        }
        return false
    }

    companion object {
        protected fun changeIndex(gamePiece: GamePiece?, index: Int): GamePiece? {
            return when (gamePiece) {
                is GameBishop -> gamePiece.copy(index = index)
                is GameKing -> gamePiece.copy(index = index)
                is GameKnight -> gamePiece.copy(index = index)
                is GamePawn -> gamePiece.copy(index = index)
                is GameQueen -> gamePiece.copy(index = index)
                is GameRook -> gamePiece.copy(index = index)
                else -> gamePiece
            }
        }
    }

    fun toSharedPiece(): Piece = when (this) {
        is GameBishop -> Bishop(pieceColor)
        is GameKing -> King(pieceColor)
        is GameKnight -> Knight(pieceColor)
        is GamePawn -> Pawn(pieceColor)
        is GameQueen -> Queen(pieceColor)
        is GameRook -> Rook(pieceColor)
    }
}

data class GamePawn(
    val index: Int,
    val color: PieceColor,
    val isFirstMoveDone: Boolean = false,
) : GamePiece(color, index) {
    private val direction: Int
        get() = if(pieceColor == PieceColor.White) 1 else -1

    override fun getAvailableMovesAt(board: List<GamePiece?>): List<Int> {
        val result = mutableListOf<Int>()

        val currentRow = index.row
        val currentColumn = index.column

        val nextRow = currentRow + direction
        val nextColumns = listOf(currentColumn-1, currentColumn, currentColumn+1)
            .filter { it in 0..7 }
            .filter {
                if(it != currentColumn) {
                    // Opposite piece is available in diagonal
                    !board.isEmptyCell(nextRow, it) && board.canPlace(nextRow, it, pieceColor)
                } else {
                    board.isEmptyCell(nextRow, it)
                }
            }

        if(nextRow in 0..7) {
            nextColumns.forEach {
                result.add(nextRow rowAndColumn it)
            }
        }

        if(!isFirstMoveDone) {
            val twoStepIndex = (currentRow + 2*direction) rowAndColumn currentColumn
            if(board.isEmptyCell(twoStepIndex.row, twoStepIndex.column))
                result.add(twoStepIndex)
        }

        return result
    }
}

data class GameKing(
    val index: Int,
    val color: PieceColor,
) : GamePiece(color, index) {
    override fun getAvailableMovesAt(board: List<GamePiece?>): List<Int> {
        val result = mutableListOf<Int>()

        val currentRow = index.row
        val currentColumn = index.column

        for (row in currentRow-1..currentRow+1) {
            if(row in 0..7) {
                for(column in currentColumn-1..currentColumn+1) {
                    if(column in 0..7) {
                        val i = row rowAndColumn column
                        // Either no piece OR Opposite color
                        if(board.canPlace(row, column, pieceColor)) {
                            result.add(i)
                        }
                    }
                }
            }
        }

        return result
    }
}

data class GameQueen(
    val index: Int,
    val color: PieceColor,
) : GamePiece(color, index) {
    override fun getAvailableMovesAt(board: List<GamePiece?>): List<Int> {
        return getVerticalMoves(index, board) + getHorizontalMoves(index, board) + getDiagonalMoves(index, board) + getAntiDiagonalMoves(index, board)
    }
}

data class GameBishop(
    val index: Int,
    val color: PieceColor,
) : GamePiece(color, index) {
    override fun getAvailableMovesAt(board: List<GamePiece?>): List<Int> {
        return getDiagonalMoves(index, board) + getAntiDiagonalMoves(index, board)
    }
}

data class GameKnight(
    val index: Int,
    val color: PieceColor,
) : GamePiece(color, index) {
    override fun getAvailableMovesAt(board: List<GamePiece?>): List<Int> {
        return getLMoves(index, board)
    }
}

data class GameRook(
    val index: Int,
    val color: PieceColor,
) : GamePiece(color, index) {
    override fun getAvailableMovesAt(board: List<GamePiece?>): List<Int> {
        return getVerticalMoves(index, board) + getHorizontalMoves(index, board)
    }
}