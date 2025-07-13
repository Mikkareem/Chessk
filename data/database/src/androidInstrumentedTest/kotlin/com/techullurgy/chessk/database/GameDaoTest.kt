package com.techullurgy.chessk.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.techullurgy.chessk.data.database.ChessKDatabase
import com.techullurgy.chessk.data.database.daos.GameDao
import com.techullurgy.chessk.data.database.models.GameEntity
import com.techullurgy.chessk.data.database.models.MemberEntity
import com.techullurgy.chessk.data.database.models.TimerEntity
import com.techullurgy.chessk.shared.models.Bishop
import com.techullurgy.chessk.shared.models.King
import com.techullurgy.chessk.shared.models.Knight
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.Pawn
import com.techullurgy.chessk.shared.models.PieceColorShared
import com.techullurgy.chessk.shared.models.Queen
import com.techullurgy.chessk.shared.models.Rook
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GameDaoTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var database: ChessKDatabase
    private lateinit var gameDao: GameDao

    private val initialBoard = List(64) {
        when(it) {
            0, 7 -> Rook(PieceColorShared.White)
            1, 6 -> Knight(PieceColorShared.White)
            2, 5 -> Bishop(PieceColorShared.White)
            3 -> King(PieceColorShared.White)
            4 -> Queen(PieceColorShared.White)
            in 8..15 -> Pawn(PieceColorShared.White)
            in 48..55 -> Pawn(PieceColorShared.Black)
            56, 63 -> Rook(PieceColorShared.Black)
            57, 62 -> Knight(PieceColorShared.Black)
            58, 61 -> Bishop(PieceColorShared.Black)
            59 -> Queen(PieceColorShared.Black)
            60 -> King(PieceColorShared.Black)
            else -> null
        }
    }

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(context, ChessKDatabase::class.java).build()
        gameDao = database.gameDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertGame() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            roomName = "",
            roomDescription = "",
            createdBy = "",
            board = initialBoard,
            assignedColor = PieceColorShared.Black,
            currentPlayer = PieceColorShared.White
        )

        gameDao.insertGame(entity)

        val proj = gameDao.observeGames().first().first()
        assertEquals(entity.roomId, proj.game.roomId)
    }

    @Test
    fun testIsMyTurn_FalseWorking_WhenAssignedColorIsBlack() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            roomName = "",
            roomDescription = "",
            createdBy = "",
            board = initialBoard,
            assignedColor = PieceColorShared.Black,
            currentPlayer = PieceColorShared.White
        )
        gameDao.insertGame(entity)

        val proj = gameDao.observeGames().first().first()

        val isMyTurn = proj.game.currentPlayer == proj.game.assignedColor

        assertEquals(false, isMyTurn)
    }

    @Test
    fun testIsMyTurn_TrueWorking_WhenAssignedColorIsBlack() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            roomName = "",
            roomDescription = "",
            createdBy = "",
            board = initialBoard,
            assignedColor = PieceColorShared.Black,
            currentPlayer = PieceColorShared.Black
        )
        gameDao.insertGame(entity)

        val proj = gameDao.observeGames().first().first()

        val isMyTurn = proj.game.currentPlayer == proj.game.assignedColor

        assertEquals(true, isMyTurn)
    }

    @Test
    fun testIsMyTurn_FalseWorking_WhenAssignedColorIsWhite() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            roomName = "",
            roomDescription = "",
            createdBy = "",
            board = initialBoard,
            assignedColor = PieceColorShared.White,
            currentPlayer = PieceColorShared.Black
        )
        gameDao.insertGame(entity)

        val proj = gameDao.observeGames().first().first()

        val isMyTurn = proj.game.currentPlayer == proj.game.assignedColor

        assertEquals(false, isMyTurn)
    }

    @Test
    fun testIsMyTurn_TrueWorking_WhenAssignedColorIsWhite() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            roomName = "",
            roomDescription = "",
            createdBy = "",
            board = initialBoard,
            assignedColor = PieceColorShared.White,
            currentPlayer = PieceColorShared.White
        )
        gameDao.insertGame(entity)

        val proj = gameDao.observeGames().first().first()

        val isMyTurn = proj.game.currentPlayer == proj.game.assignedColor

        assertEquals(true, isMyTurn)
    }

    @Test
    fun testYourTimeAndOpponentTime_Working_WhenAssignedColorIsBlack() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            roomName = "",
            roomDescription = "",
            createdBy = "",
            board = initialBoard,
            assignedColor = PieceColorShared.Black,
            currentPlayer = PieceColorShared.White
        )
        gameDao.insertGame(entity)

        val timerEntity = TimerEntity(
            roomId = entity.roomId,
            whiteTime = 100,
            blackTime = 200
        )
        gameDao.updateTimer(timerEntity)

        val proj = gameDao.observeGames().first().first()

        assertEquals(proj.timer?.blackTime, 200)
        assertEquals(proj.timer?.whiteTime, 100)
    }

    @Test
    fun testYourTimeAndOpponentTime_Working_WhenAssignedColorIsWhite() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            roomName = "",
            roomDescription = "",
            createdBy = "",
            board = initialBoard,
            assignedColor = PieceColorShared.White,
            currentPlayer = PieceColorShared.Black
        )
        gameDao.insertGame(entity)

        val timerEntity = TimerEntity(
            roomId = entity.roomId,
            whiteTime = 100,
            blackTime = 200
        )
        gameDao.updateTimer(timerEntity)

        val proj = gameDao.observeGames().first().first()

        assertEquals(proj.timer?.whiteTime, 100)
        assertEquals(proj.timer?.blackTime, 200)
    }

    @Test
    fun testTypeConverters_Working_ForGameEntity() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            roomName = "",
            roomDescription = "",
            createdBy = "",
            board = initialBoard,
            assignedColor = PieceColorShared.White,
            currentPlayer = PieceColorShared.Black,
            lastMove = MoveShared(12, 15),
            availableMoves = listOf(
                MoveShared(23, 24),
                MoveShared(23, 28),
                MoveShared(23, 32),
                MoveShared(23, 36),
                MoveShared(23, 21)
            ),
            cutPieces = setOf(Rook(PieceColorShared.White), Pawn(PieceColorShared.White))
        )
        gameDao.insertGame(entity)

        val proj = gameDao.observeGame(entity.roomId).first()!!

        assertEquals(Rook(PieceColorShared.White), proj.game.board.first())
        assertEquals(Pawn(PieceColorShared.White), proj.game.board[11])
        assertEquals(null, proj.game.board[22])
        assertEquals(null, proj.game.board[42])
        assertEquals(Pawn(PieceColorShared.Black), proj.game.board[53])
        assertEquals(Rook(PieceColorShared.Black), proj.game.board.last())
        assertEquals(MoveShared(12, 15), proj.game.lastMove)
        assertEquals(MoveShared(23, 28), proj.game.availableMoves?.get(1))
        assertEquals(Rook(PieceColorShared.White), proj.game.cutPieces?.first())

        val nullCounts = proj.game.board.count { it == null }
        assertEquals(32, nullCounts)

        val first16 = proj.game.board.take(16)
        val last16 = proj.game.board.takeLast(16)
        val first16AndLast16 = first16 + last16

        assertEquals(0, first16AndLast16.count { it == null })
        assertTrue {
            first16.all { it!!.color == PieceColorShared.White } &&
                    last16.all { it!!.color == PieceColorShared.Black }
        }
    }

    @Test
    fun testResetSelection_Working() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            roomName = "",
            roomDescription = "",
            createdBy = "",
            board = initialBoard,
            assignedColor = PieceColorShared.White,
            currentPlayer = PieceColorShared.Black,
            lastMove = MoveShared(12, 15),
            availableMoves = listOf(
                MoveShared(23, 24),
                MoveShared(23, 28),
                MoveShared(23, 32),
                MoveShared(23, 36),
                MoveShared(23, 21)
            ),
            cutPieces = setOf(Rook(PieceColorShared.White), Pawn(PieceColorShared.White))
        )
        gameDao.insertGame(entity)

        gameDao.resetSelection(entity.roomId)

        val proj2 = gameDao.observeGame(entity.roomId).first()

        assertNotNull(proj2)

        assertEquals(-1, proj2.game.selectedIndex)
        assertEquals(null, proj2.game.availableMoves)
    }

    @Test
    fun testNewObserveGame() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            roomName = "",
            roomDescription = "",
            createdBy = "",
            board = initialBoard,
            assignedColor = PieceColorShared.White,
            currentPlayer = PieceColorShared.Black,
            lastMove = MoveShared(12, 15),
            availableMoves = listOf(
                MoveShared(23, 24),
                MoveShared(23, 28),
                MoveShared(23, 32),
                MoveShared(23, 36),
                MoveShared(23, 21)
            ),
            cutPieces = setOf(Rook(PieceColorShared.White), Pawn(PieceColorShared.White))
        )
        gameDao.insertGame(entity)

        val timerEntity = TimerEntity(
            roomId = entity.roomId,
            whiteTime = 100,
            blackTime = 200
        )
        gameDao.updateTimer(timerEntity)

        val membersEntity = listOf(
            MemberEntity(
                roomId = entity.roomId,
                name = "Irsath",
                userId = "I1",
                profilePicUrl = "U1",
                assignedColor = PieceColorShared.White,
                isOwner = true
            ),
            MemberEntity(
                roomId = entity.roomId,
                name = "Kareem",
                userId = "K1",
                profilePicUrl = "U2",
                assignedColor = PieceColorShared.Black,
                isOwner = false
            ),
            MemberEntity(
                roomId = entity.roomId + "uiasudy",
                name = "Farook",
                userId = "F1",
                profilePicUrl = "U3",
                assignedColor = PieceColorShared.Black,
                isOwner = true
            ),
        )

        membersEntity.forEach { gameDao.updateMember(it) }

        val result = gameDao.observeGames().first().first()

        assertNotNull(result)
        assertTrue {
            result.members.all { it.roomId == entity.roomId }
        }
    }
}