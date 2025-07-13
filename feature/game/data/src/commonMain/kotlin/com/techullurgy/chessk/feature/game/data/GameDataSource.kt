package com.techullurgy.chessk.feature.game.data

import com.techullurgy.chessk.data.database.DatabaseDataSource
import com.techullurgy.chessk.feature.game.data.mappers.asGameRoom
import com.techullurgy.chessk.feature.game.models.BrokerEvent
import com.techullurgy.chessk.feature.game.models.GameRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GameDataSource {
    val broker: MessageBroker<BrokerEvent>

    fun observeJoinedGames(): Flow<List<GameRoom>>

    fun observeGame(roomId: String): Flow<GameRoom?>
}

internal class GameDataSourceImpl(
    private val dbDataSource: DatabaseDataSource,
    override val broker: MessageBroker<BrokerEvent>
) : GameDataSource {

    override fun observeGame(roomId: String): Flow<GameRoom?> =
        dbDataSource.observeGame(roomId).map {
            if (it == null) return@map null
            with(it.game) {
                with(it.members) {
                    with(it.timer) {
                        asGameRoom()
                    }
                }
            }
        }


    override fun observeJoinedGames(): Flow<List<GameRoom>> =
        dbDataSource.observeGames().map { list ->
            list.mapNotNull {
                with(it.game) {
                    with(it.members) {
                        with(it.timer) {
                            asGameRoom()
                        }
                    }
                }
            }
        }
}