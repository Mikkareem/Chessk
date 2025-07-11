package com.techullurgy.chessk.data.database

import com.techullurgy.chessk.data.database.daos.GameDao
import com.techullurgy.chessk.data.database.models.GameEntity
import com.techullurgy.chessk.data.database.models.MemberEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DatabaseDataSource(
    private val gameDao: GameDao
) {
    fun isGameRoomsAvailable(): Flow<Boolean> =
        gameDao.observeGamesList().map { it.isNotEmpty() }.distinctUntilChanged()

    suspend fun invalidateGameRooms() = gameDao.invalidateJoinedRooms()

    suspend fun saveJoinedRoomsDetails(
        gameEntities: List<GameEntity>,
        memberEntities: List<List<MemberEntity>>
    ) {
        gameEntities.forEach { gameDao.insertGame(it) }
        memberEntities.forEach { it.forEach { m -> gameDao.updateMember(m) } }
    }

    fun observeJoinedGamesList(): Flow<List<Pair<GameEntity, List<MemberEntity>>>> {
        TODO("Not yet implemented")
    }

    fun observeGame(): Flow<Pair<GameEntity, List<MemberEntity>>> {
        TODO("Not yet implemented")
    }
}