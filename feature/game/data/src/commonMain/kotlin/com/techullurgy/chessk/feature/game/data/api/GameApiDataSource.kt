package com.techullurgy.chessk.feature.game.data.api

import com.techullurgy.chessk.feature.game.domain.remote.ChessGameApi
import com.techullurgy.chessk.shared.models.GameRoom

internal class GameApiDataSource(
    private val gameApi: ChessGameApi
) {
    val isSocketActive get() = gameApi.isSocketActive

    val gameEventsFlow get() = gameApi.gameEventsFlow

    fun startSession() = gameApi.startSession()
    fun stopSession() = gameApi.stopSession()

    suspend fun createRoom(room: GameRoom) {
        gameApi.createRoom(room)
    }

    suspend fun joinRoom(roomId: String) {
        gameApi.joinRoom(roomId)
    }

    suspend fun getCreatedRoomsByMe(): List<GameRoom> {
        return gameApi.getCreatedRoomsByMe()
    }

    suspend fun fetchAnyJoinedRoomsAvailable() = gameApi.fetchAnyJoinedRoomsAvailable()
}