package dev.techullurgy.chess.domain

import dev.techullurgy.chess.events.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

class Room(
    val id: String,
    val name: String,
    val description: String,
    val createdBy: String
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val game = Game(coroutineScope)

    private val players = ConcurrentHashMap<Color, Player>()

    private var timerJob: Job? = null

    private var isStarted: Boolean = false

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

    suspend fun startGame() {
        isStarted = true
        observeBoardStateAndBroadcast()
        whitePlayerSend(getGameStartedEvent().copy(assignedColor = Color.White))
        blackPlayerSend(getGameStartedEvent().copy(assignedColor = Color.Black))
        runTimer()
    }

    fun playerEntered(clientId: String) {
        sendCurrentBoardStateToPlayer(clientId)
    }

    fun cellSelectedForMove(data: CellSelection) {
        if(data.color != game.currentPlayerColor) return

        val availableIndices = game.cellSelectedForMove(data.selectedIndex)
        sendToCurrentPlayer(SelectionResult(roomId = id, availableIndices = availableIndices, selectedIndex = data.selectedIndex))
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

    private fun getGameStartedEvent() = GameStarted(
        roomId = id,
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
                    whiteTime = if(activePlayer.colorAssigned == Color.White) activePlayer.timeLeft.inWholeSeconds else inactivePlayer.timeLeft.inWholeSeconds,
                    blackTime = if(activePlayer.colorAssigned == Color.Black) activePlayer.timeLeft.inWholeSeconds else inactivePlayer.timeLeft.inWholeSeconds
                )
                broadcast(timerUpdate)
                delay(1000)
            }
        }
    }

    private fun sendCurrentBoardStateToPlayer(clientId: String) {
        getAssignedPlayers().find { it.user.clientId == clientId }?.also {
            if(isStarted) {
                it.sendEvent(getGameStartedEvent().copy(assignedColor = it.colorAssigned))
                it.sendEvent(game.boardState.value.toGameUpdate())
            }
        }
    }

    private fun observeBoardStateAndBroadcast() {
        coroutineScope.launch {
            game.boardState.collectLatest {
                broadcast(it.toGameUpdate())
            }
        }
    }

    private suspend fun broadcast(event: SenderBaseEvent) {
        supervisorScope {
            launch { players[Color.White]?.sendEvent(event) }
            launch { players[Color.Black]?.sendEvent(event) }
        }
    }

    private fun sendToCurrentPlayer(event: SenderBaseEvent) {
        if(game.currentPlayerColor == Color.White) {
            whitePlayerSend(event)
        } else {
            blackPlayerSend(event)
        }
    }

    private fun whitePlayerSend(event: SenderBaseEvent) = players[Color.White]?.sendEvent(event)

    private fun blackPlayerSend(event: SenderBaseEvent) = players[Color.Black]?.sendEvent(event)

    fun invalidateRoom() {
        coroutineScope.cancel()
    }
}