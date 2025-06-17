package com.techullurgy.chessk.feature.game.data

import com.techullurgy.chessk.database.models.MemberEntity
import com.techullurgy.chessk.database.models.TimerEntity
import com.techullurgy.chessk.feature.game.data.api.GameApiDataSource
import com.techullurgy.chessk.feature.game.data.db.GameDbDataSource
import com.techullurgy.chessk.feature.game.domain.events.GameStartedEvent
import com.techullurgy.chessk.feature.game.domain.events.GameUpdateEvent
import com.techullurgy.chessk.feature.game.domain.events.ResetSelectionDoneEvent
import com.techullurgy.chessk.feature.game.domain.events.SelectionResultEvent
import com.techullurgy.chessk.feature.game.domain.events.ServerGameEvent
import com.techullurgy.chessk.feature.game.domain.events.TimerUpdateEvent
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform

internal class GameRoomMessageBroker(
    private val gameDbDataSource: GameDbDataSource,
    private val gameApiDataSource: GameApiDataSource,
) {
    fun observeWebsocketConnection() =
        observeDesiredGameEntities()
            .transform {
                if(!it.isEmpty()) {
                    if(!gameApiDataSource.isSocketActive) {
                        emit(true)
                    }
                } else {
                    if(gameApiDataSource.isSocketActive) {
                        emit(false)
                    }
                }
            }
            .distinctUntilChanged()
            .onEach {
                if(it) {
                    gameApiDataSource.startSession()
                } else {
                    gameApiDataSource.stopSession()
                }
            }

    fun observeAndUpdateGameEventDatabase() =
        gameApiDataSource.gameEventsFlow
            .transform {
                if(it is ServerGameEvent) {
                    when(it) {
                        is GameStartedEvent -> {
                            gameDbDataSource.gameStartedUpdate(
                                roomId = it.roomId,
                                members = it.members.map { member ->
                                    MemberEntity(
                                        roomId = it.roomId,
                                        name = member.name,
                                        profilePicUrl = member.profilePicUrl,
                                        assignedColor = member.assignedColor,
                                        userId = member.userId
                                    )
                                },
                                assignedColor = it.assignedColor
                            )
                        }
                        is GameUpdateEvent -> {
                            gameDbDataSource.updateGame(
                                roomId = it.roomId,
                                board = it.board,
                                lastMove = it.lastMove,
                                currentPlayer = it.currentTurn,
                                cutPieces = it.cutPieces,
                                kingInCheckIndex = it.kingInCheckIndex,
                            )
                        }
                        is ResetSelectionDoneEvent -> gameDbDataSource.resetSelection(roomId = it.roomId)
                        is SelectionResultEvent -> gameDbDataSource.updateAvailableMoves(
                            roomId = it.roomId,
                            selectedIndex = it.selectedIndex,
                            availableMoves = it.availableMoves
                        )
                        is TimerUpdateEvent -> gameDbDataSource.updateTimer(
                            TimerEntity(
                                roomId = it.roomId,
                                whiteTime = it.whiteTime,
                                blackTime = it.blackTime
                            )
                        )
                    }
                } else {
                    emit(it)
                }
            }

    private fun observeDesiredGameEntities() =
        gameDbDataSource.observeGamesList()
            .distinctUntilChanged()
}