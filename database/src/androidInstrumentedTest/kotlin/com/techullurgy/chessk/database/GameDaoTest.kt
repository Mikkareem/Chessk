package com.techullurgy.chessk.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.techullurgy.chessk.database.daos.GameDao
import com.techullurgy.chessk.database.models.GameEntity
import com.techullurgy.chessk.database.models.TimerEntity
import com.techullurgy.chessk.shared.models.Bishop
import com.techullurgy.chessk.shared.models.King
import com.techullurgy.chessk.shared.models.Knight
import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.models.Pawn
import com.techullurgy.chessk.shared.models.PieceColor
import com.techullurgy.chessk.shared.models.Queen
import com.techullurgy.chessk.shared.models.Rook
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameDaoTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var database: ChessKDatabase
    private lateinit var gameDao: GameDao

    private val initialBoard = List(64) {
        when(it) {
            0, 7 -> Rook(PieceColor.White)
            1, 6 -> Knight(PieceColor.White)
            2, 5 -> Bishop(PieceColor.White)
            3 -> King(PieceColor.White)
            4 -> Queen(PieceColor.White)
            in 8..15 -> Pawn(PieceColor.White)
            in 48..55 -> Pawn(PieceColor.Black)
            56, 63 -> Rook(PieceColor.Black)
            57, 62 -> Knight(PieceColor.Black)
            58, 61 -> Bishop(PieceColor.Black)
            59 -> Queen(PieceColor.Black)
            60 -> King(PieceColor.Black)
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
            board = initialBoard,
            assignedColor = PieceColor.Black,
            currentPlayer = PieceColor.White
        )

        gameDao.insertGame(entity)

        val proj = gameDao.observeGamesList().first().first()
        assertEquals(entity.roomId, proj.roomId)
    }

    @Test
    fun testIsMyTurn_FalseWorking_WhenAssignedColorIsBlack() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            board = initialBoard,
            assignedColor = PieceColor.Black,
            currentPlayer = PieceColor.White
        )
        gameDao.insertGame(entity)

        val proj = gameDao.observeGamesList().first().first()

        assertEquals(false, proj.isMyTurn)
    }

    @Test
    fun testIsMyTurn_TrueWorking_WhenAssignedColorIsBlack() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            board = initialBoard,
            assignedColor = PieceColor.Black,
            currentPlayer = PieceColor.Black
        )
        gameDao.insertGame(entity)

        val proj = gameDao.observeGamesList().first().first()

        assertEquals(true, proj.isMyTurn)
    }

    @Test
    fun testIsMyTurn_FalseWorking_WhenAssignedColorIsWhite() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            board = initialBoard,
            assignedColor = PieceColor.White,
            currentPlayer = PieceColor.Black
        )
        gameDao.insertGame(entity)

        val proj = gameDao.observeGamesList().first().first()

        assertEquals(false, proj.isMyTurn)
    }

    @Test
    fun testIsMyTurn_TrueWorking_WhenAssignedColorIsWhite() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            board = initialBoard,
            assignedColor = PieceColor.White,
            currentPlayer = PieceColor.White
        )
        gameDao.insertGame(entity)

        val proj = gameDao.observeGamesList().first().first()

        assertEquals(true, proj.isMyTurn)
    }

    @Test
    fun testYourTimeAndOpponentTime_InitialValue() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            board = initialBoard,
            assignedColor = PieceColor.Black,
            currentPlayer = PieceColor.White
        )
        gameDao.insertGame(entity)

        val proj = gameDao.observeGamesList().first().first()

        assertEquals(0, proj.yourTime)
        assertEquals(0, proj.opponentTime)
    }

    @Test
    fun testYourTimeAndOpponentTime_Working_WhenAssignedColorIsBlack() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            board = initialBoard,
            assignedColor = PieceColor.Black,
            currentPlayer = PieceColor.White
        )
        gameDao.insertGame(entity)

        val timerEntity = TimerEntity(
            roomId = entity.roomId,
            whiteTime = 100,
            blackTime = 200
        )
        gameDao.updateTimer(timerEntity)

        val proj = gameDao.observeGamesList().first().first()

        assertEquals(proj.yourTime, 200)
        assertEquals(proj.opponentTime, 100)
    }

    @Test
    fun testYourTimeAndOpponentTime_Working_WhenAssignedColorIsWhite() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            board = initialBoard,
            assignedColor = PieceColor.White,
            currentPlayer = PieceColor.Black
        )
        gameDao.insertGame(entity)

        val timerEntity = TimerEntity(
            roomId = entity.roomId,
            whiteTime = 100,
            blackTime = 200
        )
        gameDao.updateTimer(timerEntity)

        val proj = gameDao.observeGamesList().first().first()

        assertEquals(proj.yourTime, 100)
        assertEquals(proj.opponentTime, 200)
    }

    @Test
    fun testTypeConverters_Working_ForGameEntity() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            board = initialBoard,
            assignedColor = PieceColor.White,
            currentPlayer = PieceColor.Black,
            lastMove = Move(12, 15),
            availableMoves = listOf(Move(23, 24), Move(23, 28), Move(23, 32), Move(23, 36), Move(23, 21)),
            cutPieces = setOf(Rook(PieceColor.White), Pawn(PieceColor.White))
        )
        gameDao.insertGame(entity)

        val proj = gameDao.observeGame(entity.roomId).first()

        assertEquals(Rook(PieceColor.White), proj.board.first())
        assertEquals(Pawn(PieceColor.White), proj.board[11])
        assertEquals(null, proj.board[22])
        assertEquals(null, proj.board[42])
        assertEquals(Pawn(PieceColor.Black), proj.board[53])
        assertEquals(Rook(PieceColor.Black), proj.board.last())
        assertEquals(Move(12, 15), proj.lastMove)
        assertEquals(Move(23, 28), proj.availableMoves?.get(1))
        assertEquals(Rook(PieceColor.White), proj.cutPieces?.first())

        val nullCounts = proj.board.count { it == null }
        assertEquals(32, nullCounts)

        val first16 = proj.board.take(16)
        val last16 = proj.board.takeLast(16)
        val first16AndLast16 = first16 + last16

        assertEquals(0, first16AndLast16.count { it == null })
        assertTrue {
            first16.all { it!!.color == PieceColor.White } &&
                    last16.all { it!!.color == PieceColor.Black }
        }
    }

    @Test
    fun testResetSelection_Working() = runBlocking {
        val entity = GameEntity(
            roomId = "12318623",
            board = initialBoard,
            assignedColor = PieceColor.White,
            currentPlayer = PieceColor.Black,
            lastMove = Move(12, 15),
            availableMoves = listOf(Move(23, 24), Move(23, 28), Move(23, 32), Move(23, 36), Move(23, 21)),
            cutPieces = setOf(Rook(PieceColor.White), Pawn(PieceColor.White))
        )
        gameDao.insertGame(entity)

        gameDao.resetSelection(entity.roomId)

        val proj2 = gameDao.observeGame(entity.roomId).first()

        assertEquals(-1, proj2.selectedIndex)
        assertEquals(null, proj2.availableMoves)
    }
}