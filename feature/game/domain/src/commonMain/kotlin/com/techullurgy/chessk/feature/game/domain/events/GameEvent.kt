package com.techullurgy.chessk.feature.game.domain.events

import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.core.models.Piece
import com.techullurgy.chessk.shared.models.Member
import com.techullurgy.chessk.shared.models.PieceColor

sealed interface GameEvent

sealed interface ServerGameEvent: GameEvent
sealed interface ClientGameEvent: GameEvent
sealed interface NetworkRelatedEvent: GameEvent
sealed interface UnknownEvent: GameEvent

data class GameStartedEvent(
    val roomId: String,
    val members: List<Member>,
    val assignedColor: PieceColor
): ServerGameEvent

data class ResetSelectionDoneEvent(
    val roomId: String
): ServerGameEvent

data class SelectionResultEvent(
    val roomId: String,
    val availableMoves: List<Move>,
    val selectedIndex: Int
): ServerGameEvent

data class TimerUpdateEvent(
    val roomId: String,
    val whiteTime: Long,
    val blackTime: Long
): ServerGameEvent

data class GameUpdateEvent(
    val roomId: String,
    val board: List<Piece?>,
    val currentTurn: PieceColor,
    val lastMove: Move?,
    val cutPieces: Set<Piece>?,
    val kingInCheckIndex: Int?,
): ServerGameEvent

data class CellSelectionEvent(
    val roomId: String,
    val color: PieceColor,
    val selectedIndex: Int
): ClientGameEvent

data class DisconnectedEvent(
    val roomId: String
): ClientGameEvent

data class EnterRoomEvent(
    val roomId: String
): ClientGameEvent

data class PieceMoveEvent(
    val roomId: String,
    val color: PieceColor,
    val from: Int,
    val to: Int
): ClientGameEvent

data class ResetSelectionEvent(
    val roomId: String
): ClientGameEvent

data object RetryFetchingEvent: NetworkRelatedEvent
data object NetworkNotAvailableEvent: NetworkRelatedEvent
data object UserConnectedEvent: NetworkRelatedEvent
data object UserDisconnectedEvent: NetworkRelatedEvent
data object NetworkLoadingEvent: NetworkRelatedEvent
data object GameNotAvailableEvent: UnknownEvent
data object NotYetAnyEventAvailableEvent: UnknownEvent
data object SomethingWentWrongEvent: UnknownEvent