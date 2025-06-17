package com.techullurgy.chessk.feature.game.domain.remote

import com.techullurgy.chessk.feature.game.domain.events.ClientGameEvent
import com.techullurgy.chessk.feature.game.domain.events.GameEvent
import com.techullurgy.chessk.shared.models.GameRoom
import kotlinx.coroutines.flow.Flow

interface ChessGameApi {
    val isSocketActive: Boolean

    val gameEventsFlow: Flow<GameEvent>

    fun startSession()
    fun stopSession()

    fun sendEvent(event: ClientGameEvent)

    suspend fun fetchAnyJoinedRoomsAvailable(): List<GameRoom>
    suspend fun getCreatedRoomsByMe(): List<GameRoom>
    suspend fun joinRoom(roomId: String)
    suspend fun createRoom(room: GameRoom)

    companion object {
        private const val HOST_AND_PORT = "192.168.225.184:8080"

        const val HTTP_BASE_URL = "http://$HOST_AND_PORT"
        const val WS_BASE_URL = "ws://$HOST_AND_PORT"
    }
}