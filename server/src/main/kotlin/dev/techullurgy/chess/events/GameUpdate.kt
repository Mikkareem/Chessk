package dev.techullurgy.chess.events

import dev.techullurgy.chess.domain.Color
import dev.techullurgy.chess.domain.EncodedBoardState
import dev.techullurgy.chess.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_GAME_UPDATE)
data class GameUpdate(
    val board: String,
    val currentTurn: Color,
    val lastMove: String?,
    val cutPieces: String?,
    val kingInCheckIndex: Int?,
    val gameOver: Boolean
): SenderBaseEvent

fun EncodedBoardState.toGameUpdate() = GameUpdate(
    board = board,
    currentTurn = currentTurn,
    cutPieces = cutPieces,
    lastMove = lastMove,
    kingInCheckIndex = kingInCheckIndex,
    gameOver = gameOver
)