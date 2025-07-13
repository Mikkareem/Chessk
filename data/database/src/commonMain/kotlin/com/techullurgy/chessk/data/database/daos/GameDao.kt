package com.techullurgy.chessk.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.techullurgy.chessk.data.database.models.BoardPieces
import com.techullurgy.chessk.data.database.models.CutPieces
import com.techullurgy.chessk.data.database.models.GameEntity
import com.techullurgy.chessk.data.database.models.MemberEntity
import com.techullurgy.chessk.data.database.models.TimerEntity
import com.techullurgy.chessk.data.database.models.projections.GameWithMembersAndTimer
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.PieceColorShared
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Transaction
    @Query("SELECT * FROM GameEntity")
    fun observeGames(): Flow<List<GameWithMembersAndTimer>>

    @Transaction
    @Query("SELECT * FROM GameEntity Where roomId = :roomId")
    fun observeGame(roomId: String): Flow<GameWithMembersAndTimer?>

    @Query(
        """
            UPDATE GameEntity
            SET board = :board,
                cutPieces = :cutPieces,
                lastMove = :lastMove,
                currentPlayer = :currentPlayer,
                selectedIndex = -1,
                availableMoves = null,
                kingInCheckIndex = :kingInCheckIndex
            WHERE roomId = :roomId
        """
    )
    suspend fun updateGame(
        roomId: String,
        board: BoardPieces,
        cutPieces: CutPieces?,
        lastMove: MoveShared?,
        currentPlayer: PieceColorShared,
        kingInCheckIndex: Int? = null
    )

    @Query(
        """
            UPDATE GameEntity
            SET assignedColor = :color
            WHERE roomId = :roomId
        """
    )
    suspend fun updateAssignedColor(color: PieceColorShared, roomId: String)

    @Query(
        """
            UPDATE GameEntity
            SET availableMoves = :availableMoves, 
                selectedIndex = :selectedIndex
            WHERE roomId = :roomId
        """
    )
    suspend fun updateAvailableMoves(
        roomId: String,
        selectedIndex: Int,
        availableMoves: List<MoveShared>?
    )

    @Query(
        """
            UPDATE GameEntity
            SET availableMoves = null,
                selectedIndex = -1
            WHERE roomId = :roomId
        """
    )
    suspend fun resetSelection(roomId: String)

    @Upsert
    suspend fun updateTimer(timer: TimerEntity)

    @Upsert
    suspend fun updateMember(member: MemberEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity)

    @Query("DELETE FROM GameEntity")
    suspend fun invalidateRoom()

    @Query("DELETE FROM TimerEntity")
    suspend fun invalidateTimer()

    @Query("DELETE FROM MemberEntity")
    suspend fun invalidateMembers()

    @Transaction
    suspend fun invalidateJoinedRooms() {
        invalidateMembers()
        invalidateTimer()
        invalidateRoom()
    }

    @Transaction
    suspend fun gameStartedUpdate(
        roomId: String,
        members: List<MemberEntity>,
        assignedColor: PieceColorShared
    ) {
        updateAssignedColor(assignedColor, roomId)
        members.forEach { updateMember(it) }
    }
}