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
}