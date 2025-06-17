package com.techullurgy.chessk.feature.game.data.db

import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.database.daos.GameDao
import com.techullurgy.chessk.database.models.BoardPieces
import com.techullurgy.chessk.database.models.CutPieces
import com.techullurgy.chessk.database.models.GameEntity
import com.techullurgy.chessk.database.models.TimerEntity
import com.techullurgy.chessk.database.models.MemberEntity
import com.techullurgy.chessk.shared.models.PieceColor

internal class GameDbDataSource(
    private val gameDao: GameDao
) {
    fun observeGamesList() = gameDao.observeGamesList()

    suspend fun updateGame(
        roomId: String,
        board: BoardPieces,
        lastMove: Move?,
        currentPlayer: PieceColor,
        cutPieces: CutPieces?,
        kingInCheckIndex: Int?,
    ) = gameDao.updateGame(
        roomId = roomId,
        board = board,
        cutPieces = cutPieces,
        lastMove = lastMove,
        currentPlayer = currentPlayer,
        kingInCheckIndex = kingInCheckIndex
    )

    suspend fun updateAvailableMoves(
        roomId: String,
        selectedIndex: Int,
        availableMoves: List<Move>
    ) = gameDao.updateAvailableMoves(roomId, selectedIndex, availableMoves)

    suspend fun resetSelection(roomId: String) = gameDao.resetSelection(roomId)

//    suspend fun getAssignedColor(roomId: String): PieceColor = gameDao.getAssignedColor(roomId)

    suspend fun updateTimer(timer: TimerEntity) = gameDao.updateTimer(timer)
    suspend fun insertGame(game: GameEntity) = gameDao.insertGame(game)

    suspend fun invalidateJoinedRooms() = gameDao.invalidateJoinedRooms()

    suspend fun gameStartedUpdate(
        roomId: String,
        members: List<MemberEntity>,
        assignedColor: PieceColor
    ) = gameDao.gameStartedUpdate(roomId, members, assignedColor)

    fun observeGame(roomId: String) = gameDao.observeGame(roomId)
}