package com.techullurgy.chessk.domain

import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.PieceColorShared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

typealias Board = List<GamePiece?>
typealias CutPieces = List<GamePiece>

class Game(
    roomId: String,
    private val scope: CoroutineScope
) {

    private val isGameStarted = MutableStateFlow<Boolean>(false)

    private val _boardState = MutableStateFlow(BoardState(roomId = roomId))

    @OptIn(FlowPreview::class)
    val boardState = combine(
        _boardState.map { it.encode() },
        isGameStarted
    ) { boardState, isGameStarted ->
        boardState.copy(gameStarted = isGameStarted)
    }
        .debounce(1000)
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = _boardState.value.encode()
        )

    var currentPlayerColor: PieceColorShared = PieceColorShared.White
        private set

    var selectedIndexForMove: Int = -1
        private set

    val isStarted: Boolean get() = isGameStarted.value

    fun move(to: Int) {
        val newCutPieces: CutPieces = if(_boardState.value.board[to] != null) {
            _boardState.value.cutPieces.toMutableList().apply {
                add(_boardState.value.board[to]!!)
            }.toList()
        } else _boardState.value.cutPieces

        val newPiece = when(val currentPiece = _boardState.value.board[selectedIndexForMove]!!) {
            is GameBishop -> currentPiece.copy(index = to)
            is GameKing -> currentPiece.copy(index = to)
            is GameKnight -> currentPiece.copy(index = to)
            is GamePawn -> currentPiece.copy(index = to, isFirstMoveDone = true)
            is GameQueen -> currentPiece.copy(index = to)
            is GameRook -> currentPiece.copy(index = to)
        }

        val oppositeColor =
            if (currentPlayerColor == PieceColorShared.White) PieceColorShared.Black else PieceColorShared.White
        val kingInCheckPosition = checkForOppositeKingInCheck(oppositeColor)
        val lastMove = MoveShared(selectedIndexForMove, to)

        val newBoard = _boardState.value.board.toMutableList().apply {
            this[selectedIndexForMove] = null
            this[to] = newPiece
        }

        if (checkForOppositeKingCheckMate(oppositeColor)) {
            _boardState.value = _boardState.value.copy(
//                gameOver = true,
                board = newBoard,
                cutPieces = newCutPieces,
                kingInCheckIndex = kingInCheckPosition,
                lastMove = lastMove,
            )
            return
        }

        changeTurnAndReset()

        _boardState.value = _boardState.value.copy(
//            gameOver = false,
            board = newBoard,
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

    fun start() {
        isGameStarted.value = true
    }

    private fun checkForOppositeKingInCheck(oppositeColor: PieceColorShared): Int? {
        val oppositeGameKingPosition = _boardState.value.board.filterIsInstance<GameKing>()
            .first { it.color == oppositeColor }.index

        _boardState.value.board.filterNotNull().filter { it.pieceColor != oppositeColor }
            .forEach {
                if (it.getAvailableIndices(_boardState.value.board)
                        .contains(oppositeGameKingPosition)
                ) return oppositeGameKingPosition
            }

        return null
    }

    private fun checkForOppositeKingCheckMate(oppositeColor: PieceColorShared): Boolean {
        _boardState.value.board.filterNotNull().filter { it.pieceColor == oppositeColor }
            .forEach {
                if(it.getAvailableIndices(_boardState.value.board).isNotEmpty()) return false
            }

        return true
    }

    private fun changeTurnAndReset() {
        currentPlayerColor =
            if (currentPlayerColor == PieceColorShared.White) PieceColorShared.Black else PieceColorShared.White
        selectedIndexForMove = -1
    }
}

private data class BoardState(
    val roomId: String,
    val board: Board = initialBoard(),
    val cutPieces: CutPieces = emptyList(),
    val currentTurn: PieceColorShared = PieceColorShared.White,
    val kingInCheckIndex: Int? = null,
    val lastMove: MoveShared? = null,
    val gameStarted: Boolean = false,
)

private fun BoardState.encode() = EncodedBoardState(
    roomId = roomId,
    board = board.map { it?.toSharedPiece() },
    cutPieces = cutPieces.map { it.toSharedPiece() }.toSet(),
    currentTurn = currentTurn,
    kingInCheckIndex = kingInCheckIndex,
    lastMove = lastMove,
    gameStarted = gameStarted
)

private fun initialBoard(): Board = List(8 * 8) {
    when(it.row) {
        0 -> {
            when(it.column) {
                0, 7 -> GameRook(it, PieceColorShared.White)
                1, 6 -> GameKnight(it, PieceColorShared.White)
                2, 5 -> GameBishop(it, PieceColorShared.White)
                3 -> GameQueen(it, PieceColorShared.White)
                4 -> GameKing(it, PieceColorShared.White)
                else -> null
            }
        }
        1 -> {
            GamePawn(it, PieceColorShared.White)
        }
        6 -> {
            GamePawn(it, PieceColorShared.Black)
        }
        7 -> {
            when(it.column) {
                0, 7 -> GameRook(it, PieceColorShared.Black)
                1, 6 -> GameKnight(it, PieceColorShared.Black)
                2, 5 -> GameBishop(it, PieceColorShared.Black)
                3 -> GameKing(it, PieceColorShared.Black)
                4 -> GameQueen(it, PieceColorShared.Black)
                else -> null
            }
        }
        else -> null
    }
}