package dev.techullurgy.chess.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.Serializable

typealias Board = List<Piece?>
typealias CutPieces = List<Piece>

private const val delimiter = "***"

class Game(
    private val scope: CoroutineScope
) {

    private val _boardState = MutableStateFlow(BoardState())
    val boardState = _boardState.map { it.encode() }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = _boardState.value.encode()
        )

    var currentPlayerColor: Color = Color.White
        private set

    var selectedIndexForMove: Int = -1
        private set

    fun move(to: Int) {

        val newCutPieces: CutPieces = if(_boardState.value.board[to] != null) {
            _boardState.value.cutPieces.toMutableList().apply {
                add(_boardState.value.board[to]!!)
            }.toList()
        } else _boardState.value.cutPieces

        val newPiece = when(val currentPiece = _boardState.value.board[selectedIndexForMove]!!) {
            is Bishop -> currentPiece.copy(index = to)
            is King -> currentPiece.copy(index = to)
            is Knight -> currentPiece.copy(index = to)
            is Pawn -> currentPiece.copy(index = to, isFirstMoveDone = true)
            is Queen -> currentPiece.copy(index = to)
            is Rook -> currentPiece.copy(index = to)
        }

        val oppositeColor = if(currentPlayerColor == Color.White) Color.Black else Color.White
        val kingInCheckPosition = checkForOppositeKingInCheck(oppositeColor)
        val lastMove = Pair(selectedIndexForMove, to)

        if(checkForOppositeKingCheckMate(oppositeColor)) {
            _boardState.value = _boardState.value.copy(
                gameOver = true,
                board = _boardState.value.board.toMutableList().apply {
                    this[selectedIndexForMove] = null
                    this[to] = newPiece
                },
                cutPieces = newCutPieces,
                kingInCheckIndex = kingInCheckPosition,
                lastMove = lastMove,
            )
            return
        }

        changeTurnAndReset()

        _boardState.value = _boardState.value.copy(
            gameOver = false,
            board = _boardState.value.board.toMutableList().apply {
                this[selectedIndexForMove] = null
                this[to] = newPiece
            },
            cutPieces = newCutPieces,
            kingInCheckIndex = kingInCheckPosition,
            currentTurn = currentPlayerColor,
            lastMove = lastMove,
        )
    }

    fun cellSelectedForMove(index: Int): List<Int> {
        selectedIndexForMove = index
        return _boardState.value.board[selectedIndexForMove]!!.getAvailableIndices(_boardState.value.board)
    }

    fun resetSelection() {
        selectedIndexForMove = -1
    }

    private fun checkForOppositeKingInCheck(oppositeColor: Color): Int? {
        val oppositeKingPosition = _boardState.value.board.filterIsInstance<King>().first { it.color == oppositeColor }.index

        _boardState.value.board.filterNotNull().filter { it.pieceColor != oppositeColor }
            .forEach {
                if(it.getAvailableIndices(_boardState.value.board).contains(oppositeKingPosition)) return oppositeKingPosition
            }

        return null
    }

    private fun checkForOppositeKingCheckMate(oppositeColor: Color): Boolean {
        _boardState.value.board.filterNotNull().filter { it.pieceColor == oppositeColor }
            .forEach {
                if(it.getAvailableIndices(_boardState.value.board).isNotEmpty()) return false
            }

        return true
    }

    private fun changeTurnAndReset() {
        currentPlayerColor = if (currentPlayerColor == Color.White) Color.Black else Color.White
        selectedIndexForMove = -1
    }
}

private data class BoardState(
    val board: Board = initialBoard(),
    val cutPieces: CutPieces = emptyList(),
    val currentTurn: Color = Color.White,
    val kingInCheckIndex: Int? = null,
    val gameOver: Boolean = false,
    val lastMove: Pair<Int, Int>? = null,
)

@Serializable
data class EncodedBoardState(
    val board: String,
    val cutPieces: String,
    val currentTurn: Color,
    val kingInCheckIndex: Int?,
    val gameOver: Boolean,
    val lastMove: String?,
)

private fun BoardState.encode() = EncodedBoardState(
    board = board.parsedAsBoardString(),
    cutPieces = cutPieces.parsedAsCutPiecesString(),
    currentTurn = currentTurn,
    kingInCheckIndex = kingInCheckIndex,
    gameOver = gameOver,
    lastMove = lastMove?.let { "${it.first}***${it.second}" }
)

private fun initialBoard(): Board = List(8 * 8) {
    when(it.row) {
        0 -> {
            when(it.column) {
                0,7 -> Rook(it, Color.White)
                1,6 -> Knight(it, Color.White)
                2,5 -> Bishop(it, Color.White)
                3 -> Queen(it, Color.White)
                4 -> King(it, Color.White)
                else -> null
            }
        }
        1 -> {
            Pawn(it, Color.White)
        }
        6 -> {
            Pawn(it, Color.Black)
        }
        7 -> {
            when(it.column) {
                0,7 -> Rook(it, Color.Black)
                1,6 -> Knight(it, Color.Black)
                2,5 -> Bishop(it, Color.Black)
                3 -> King(it, Color.Black)
                4 -> Queen(it, Color.Black)
                else -> null
            }
        }
        else -> null
    }
}

private fun Board.parsedAsBoardString() = joinToString(delimiter) { piece ->
    when (piece) {
        is Bishop -> if (piece.color == Color.Black) "BB" else "WB"
        is King -> if (piece.color == Color.Black) "BK" else "WK"
        is Knight -> if (piece.color == Color.Black) "BN" else "WN"
        is Pawn -> if (piece.color == Color.Black) "BP" else "WP"
        is Queen -> if (piece.color == Color.Black) "BQ" else "WQ"
        is Rook -> if (piece.color == Color.Black) "BR" else "WR"
        null -> "##"
    }
}

private fun CutPieces.parsedAsCutPiecesString() = joinToString(delimiter)