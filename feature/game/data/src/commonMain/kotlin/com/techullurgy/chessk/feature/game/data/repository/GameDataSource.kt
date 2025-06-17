package com.techullurgy.chessk.feature.game.data.repository

import com.techullurgy.chessk.database.models.GameEntity
import com.techullurgy.chessk.feature.game.data.GameRoomMessageBroker
import com.techullurgy.chessk.feature.game.data.api.GameApiDataSource
import com.techullurgy.chessk.feature.game.data.db.GameDbDataSource
import com.techullurgy.chessk.shared.models.GameRoom

internal class GameDataSource(
    private val gameDbDataSource: GameDbDataSource,
    private val gameApiDataSource: GameApiDataSource
) {
    private val broker = GameRoomMessageBroker(gameDbDataSource, gameApiDataSource)

    fun observeJoinedGamesList() = gameDbDataSource.observeGamesList()

    suspend fun invalidateJoinedRooms() = gameDbDataSource.invalidateJoinedRooms()

    fun observeGame(roomId: String) = gameDbDataSource.observeGame(roomId)

    suspend fun createRoom(room: GameRoom) {
        gameApiDataSource.createRoom(room)
    }

    suspend fun joinRoom(roomId: String) {
        gameApiDataSource.joinRoom(roomId)
    }

    suspend fun getCreatedRoomsByMe(): List<GameRoom> {
        return gameApiDataSource.getCreatedRoomsByMe()
    }

    suspend fun fetchAnyJoinedRoomsAvailable() {
        val joinedRooms = gameApiDataSource.fetchAnyJoinedRoomsAvailable()
        if(joinedRooms.isEmpty()) throw NoGamesFoundException()

        joinedRooms.forEach {
            gameDbDataSource.insertGame(
                GameEntity(
                    roomId = it.roomId,
                )
            )
        }
    }

    fun observeWebsocketConnection() = broker.observeWebsocketConnection()
    fun observeAndUpdateGameEventDatabase() = broker.observeAndUpdateGameEventDatabase()
}

class NoGamesFoundException: RuntimeException()