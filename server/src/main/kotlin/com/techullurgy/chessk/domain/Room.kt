package com.techullurgy.chessk.domain

import com.techullurgy.chessk.shared.events.CellSelection
import com.techullurgy.chessk.shared.events.GameStarted
import com.techullurgy.chessk.shared.events.PieceMove
import com.techullurgy.chessk.shared.events.ResetSelectionDone
import com.techullurgy.chessk.shared.events.SelectionResult
import com.techullurgy.chessk.shared.events.ServerToClientBaseEvent
import com.techullurgy.chessk.shared.events.TimerUpdate
import com.techullurgy.chessk.shared.models.GameRoom
import com.techullurgy.chessk.shared.models.Member
import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.models.PieceColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

class Room(
    val id: String,
    val name: String,
    val description: String,
    val createdBy: String
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val game = Game(id, coroutineScope)

    private val players = ConcurrentHashMap<PieceColor, Player>()

    private var timerJob: Job? = null

    fun addPlayer(player: Player) {
        if (players.size in 0 until 2) {
            players.put(player.colorAssigned, player)
        }
    }

    fun removePlayer(clientId: String) {
        players.entries.find { it.value.user.clientId == clientId }?.key?.let {
            players.remove(it)
        }
    }

    fun startGame() {
        game.start()
        observeBoardStateAndBroadcast()
        runTimer()
    }

    fun playerEntered(clientId: String) {
        sendCurrentBoardStateToPlayer(clientId)
    }

    fun cellSelectedForMove(data: CellSelection) {
        if(data.color != game.currentPlayerColor) return

        val availableIndices = game.cellSelectedForMove(data.selectedIndex)
        val availableMoves = availableIndices.map {
            Move(data.selectedIndex, it)
        }
        sendToCurrentPlayer(SelectionResult(roomId = id, availableMoves = availableMoves, selectedIndex = data.selectedIndex))
    }

    fun movePiece(data: PieceMove) {
        if(data.color != game.currentPlayerColor) return

        if(game.selectedIndexForMove == -1) return

        game.move(data.to)

        runTimer()
    }

    fun resetSelection() {
        game.resetSelection()
        sendToCurrentPlayer(ResetSelectionDone(roomId = id))
    }

    fun getAssignedPlayers(): Set<Player> = players.values.toSet()

    fun onUserSessionEstablished(clientId: String) {
        if (game.isStarted) {
            sendCurrentBoardStateToPlayer(clientId)
        }
    }

    private fun getGameStartedEvent(assignedColor: PieceColor) = GameStarted(
        roomId = id,
        assignedColor = assignedColor,
        members = players.entries.map {
            Member(
                name = it.value.user.userName,
                assignedColor = it.key,
                userId = it.value.user.userId,
                profilePicUrl = it.value.user.profilePicUrl
            )
        }
    )

    private fun runTimer() {
        timerJob?.cancel()
        timerJob = coroutineScope.launch {
            val activePlayer = players.values.find { it.colorAssigned == game.currentPlayerColor } ?: return@launch
            val inactivePlayer = players.values.find { it.colorAssigned != game.currentPlayerColor } ?: return@launch

            while(isActive) {
                activePlayer.timeLeft -= 1.seconds
                val timerUpdate = TimerUpdate(
                    roomId = id,
                    whiteTime = if(activePlayer.colorAssigned == PieceColor.White) activePlayer.timeLeft.inWholeSeconds else inactivePlayer.timeLeft.inWholeSeconds,
                    blackTime = if(activePlayer.colorAssigned == PieceColor.Black) activePlayer.timeLeft.inWholeSeconds else inactivePlayer.timeLeft.inWholeSeconds
                )
                broadcast(timerUpdate)
                delay(1000)
            }
        }
    }

    private fun sendCurrentBoardStateToPlayer(clientId: String) {
        getAssignedPlayers().find { it.user.clientId == clientId }?.also {
            it.sendEvent(getGameStartedEvent(it.colorAssigned))
            it.sendEvent(game.boardState.value.toGameUpdate())
        }
    }

    private fun observeBoardStateAndBroadcast() {
        coroutineScope.launch {
            game.boardState.collectLatest {
                broadcast(it.toGameUpdate())
            }
        }
    }

    private suspend fun broadcast(event: ServerToClientBaseEvent) {
        supervisorScope {
            launch { players[PieceColor.White]?.sendEvent(event) }
            launch { players[PieceColor.Black]?.sendEvent(event) }
        }
    }

    private fun sendToCurrentPlayer(event: ServerToClientBaseEvent) {
        if(game.currentPlayerColor == PieceColor.White) {
            whitePlayerSend(event)
        } else {
            blackPlayerSend(event)
        }
    }

    private fun whitePlayerSend(event: ServerToClientBaseEvent) = players[PieceColor.White]?.sendEvent(event)

    private fun blackPlayerSend(event: ServerToClientBaseEvent) = players[PieceColor.Black]?.sendEvent(event)

    fun invalidateRoom() {
        coroutineScope.cancel()
    }
}

internal fun Room.toGameRoom() = GameRoom(
    roomId = id,
    roomName = name,
    roomDescription = description,
    createdBy = createdBy
)
