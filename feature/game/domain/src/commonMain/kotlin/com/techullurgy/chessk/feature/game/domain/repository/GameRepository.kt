package com.techullurgy.chessk.feature.game.domain.repository

import com.techullurgy.chessk.feature.game.domain.events.GameState
import com.techullurgy.chessk.shared.models.GameRoom
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getJoinedGamesList(): Flow<GameState>

    fun getJoinedGame(roomId: String): Flow<GameState>

    suspend fun createRoom(room: GameRoom)
    suspend fun joinRoom(roomId: String)
    suspend fun getCreatedRoomsByMe(): List<GameRoom>

    suspend fun retry()
}