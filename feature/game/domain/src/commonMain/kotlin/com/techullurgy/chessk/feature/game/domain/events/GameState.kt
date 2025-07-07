package com.techullurgy.chessk.feature.game.domain.events

import com.techullurgy.chessk.shared.models.Member
import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.models.Piece
import com.techullurgy.chessk.shared.models.PieceColor

sealed interface GameState

data class JoinedGameState(
    val roomId: String,
    val roomName: String,
    val members: List<Member>,
    val board: List<Piece?>,
    val assignedColor: PieceColor,
    val lastMove: Move? = null,
    val isMyTurn: Boolean = false,
    val availableMoves: List<Move> = emptyList(),
    val cutPieces: Set<Piece> = emptySet(),
    val yourTime: Long = 0,
    val opponentTime: Long = 0,
): GameState

data class JoinedGameStateHeader(
    val roomId: String = "",
    val roomName: String = "",
    val membersCount: Int = 0,
    val isMyTurn: Boolean = false,
    val yourTime: Long = 0,
    val opponentTime: Long = 0,
)

data class JoinedGameStateHeaderList(
    val values: List<JoinedGameStateHeader>
): GameState

data object NetworkLoadingState: GameState
data object GameNotAvailableState: GameState
data object NetworkNotAvailableState: GameState
data object SomethingWentWrongState: GameState
data object UserDisconnectedState: GameState