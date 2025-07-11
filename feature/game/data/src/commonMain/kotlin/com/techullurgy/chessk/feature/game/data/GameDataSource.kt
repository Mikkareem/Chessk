package com.techullurgy.chessk.feature.game.data

import com.techullurgy.chessk.data.database.DatabaseDataSource
import com.techullurgy.chessk.feature.game.data.mappers.asGameRoom
import com.techullurgy.chessk.feature.game.models.GameRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GameDataSource {

    fun observeJoinedGames(): Flow<List<GameRoom>>

    fun observeGame(roomId: String): Flow<GameRoom>
}

internal class GameDataSourceImpl(
    private val dbDataSource: DatabaseDataSource
) : GameDataSource {
    override fun observeGame(roomId: String): Flow<GameRoom> = dbDataSource.observeGame().map {
        with(it.first) {
            with(it.second) {
                asGameRoom()
            }
        }
    }

    override fun observeJoinedGames(): Flow<List<GameRoom>> =
        dbDataSource.observeJoinedGamesList().map { list ->
            list.map {
                with(it.first) {
                    with(it.second) {
                        asGameRoom()
                    }
                }
            }
        }
}