package com.techullurgy.chessk.data.database

import com.techullurgy.chessk.data.database.daos.GameDao
import com.techullurgy.chessk.data.database.models.BoardPieces
import com.techullurgy.chessk.data.database.models.CutPieces
import com.techullurgy.chessk.data.database.models.GameEntity
import com.techullurgy.chessk.data.database.models.MemberEntity
import com.techullurgy.chessk.data.database.models.TimerEntity
import com.techullurgy.chessk.data.database.models.projections.GameWithMembersAndTimer
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.PieceColorShared
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DatabaseDataSource(
    private val gameDao: GameDao
) {
    fun isGameRoomsAvailable(): Flow<Boolean> =
        gameDao.observeGames().map { it.isNotEmpty() }.distinctUntilChanged()

    suspend fun invalidateGameRooms() = gameDao.invalidateJoinedRooms()

    suspend fun saveJoinedRoomsDetails(
        gameEntities: List<GameEntity>,
        memberEntities: List<List<MemberEntity>>
    ) {
        gameEntities.forEach { gameDao.insertGame(it) }
        memberEntities.forEach { it.forEach { m -> gameDao.updateMember(m) } }
    }

    suspend fun updateGame(
        roomId: String,
        board: BoardPieces,
        cutPieces: CutPieces?,
        lastMove: MoveShared?,
        currentPlayer: PieceColorShared,
        kingInCheckIndex: Int? = null
    ) = gameDao.updateGame(roomId, board, cutPieces, lastMove, currentPlayer, kingInCheckIndex)

    suspend fun updateAvailableMoves(
        roomId: String,
        selectedIndex: Int,
        availableMoves: List<MoveShared>?
    ) = gameDao.updateAvailableMoves(roomId, selectedIndex, availableMoves)

    suspend fun gameStartedUpdate(
        roomId: String,
        members: List<MemberEntity>,
        assignedColor: PieceColorShared
    ) = gameDao.gameStartedUpdate(roomId, members, assignedColor)

    suspend fun resetSelection(roomId: String) = gameDao.resetSelection(roomId)
    suspend fun updateTimer(timer: TimerEntity) = gameDao.updateTimer(timer)

    fun observeGames(): Flow<List<GameWithMembersAndTimer>> = gameDao.observeGames()

    fun observeGame(roomId: String): Flow<GameWithMembersAndTimer?> = gameDao.observeGame(roomId)
}