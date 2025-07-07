package com.techullurgy.chessk.shared

import com.techullurgy.chessk.shared.models.Pawn
import com.techullurgy.chessk.shared.models.Piece
import com.techullurgy.chessk.shared.models.PieceColor
import com.techullurgy.chessk.shared.models.Rook
import com.techullurgy.chessk.shared.utils.BoardSerializer
import com.techullurgy.chessk.shared.utils.CutPiecesSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class PieceSerializationTest {
    @Test
    fun testPiecesSerialization() {

        val game = Game(
            board = listOf(
                Pawn(PieceColor.White),
                Pawn(PieceColor.Black),
                null,
                null,
                Rook(PieceColor.White)
            ),
            cutPieces = setOf(
                Rook(PieceColor.Black),
                Rook(PieceColor.Black),
                Pawn(PieceColor.White)
            )
        )

        val encoded = Json.encodeToString(Game.serializer(), game)
        assertEquals(
            expected = """{"board":"WP***BP***.***.***WR","cutPieces":"BR***WP"}""",
            actual = encoded
        )

        val decoded = Json.decodeFromString(Game.serializer(), encoded)
        assertEquals(game, decoded)
    }

    @Test
    fun testStandalonePiecesSerialization() {
        val board = listOf(
            Pawn(PieceColor.White),
            Pawn(PieceColor.Black),
            null,
            null,
            Rook(PieceColor.White)
        )
        val expectedEncoded = "WP***BP***.***.***WR"
        val encoded = Json.encodeToString(BoardSerializer, board).removeSurrounding("\"")
        assertEquals(expectedEncoded, encoded)
        val decoded = Json.decodeFromString(BoardSerializer, "\"$expectedEncoded\"")
        assertEquals(board, decoded)
    }
}

@Serializable
data class Game(
    @Serializable(with = BoardSerializer::class)
    val board: List<Piece?> = emptyList(),
    @Serializable(with = CutPiecesSerializer::class)
    val cutPieces: Set<Piece>? = emptySet()
)