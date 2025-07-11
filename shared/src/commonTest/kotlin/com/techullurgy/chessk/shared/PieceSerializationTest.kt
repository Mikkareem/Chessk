package com.techullurgy.chessk.shared

import com.techullurgy.chessk.shared.models.Pawn
import com.techullurgy.chessk.shared.models.PieceColorShared
import com.techullurgy.chessk.shared.models.PieceShared
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
                Pawn(PieceColorShared.White),
                Pawn(PieceColorShared.Black),
                null,
                null,
                Rook(PieceColorShared.White)
            ),
            cutPieces = setOf(
                Rook(PieceColorShared.Black),
                Rook(PieceColorShared.Black),
                Pawn(PieceColorShared.White)
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
            Pawn(PieceColorShared.White),
            Pawn(PieceColorShared.Black),
            null,
            null,
            Rook(PieceColorShared.White)
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
    val board: List<PieceShared?> = emptyList(),
    @Serializable(with = CutPiecesSerializer::class)
    val cutPieces: Set<PieceShared>? = emptySet()
)