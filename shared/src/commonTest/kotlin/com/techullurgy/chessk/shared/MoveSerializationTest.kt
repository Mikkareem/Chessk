package com.techullurgy.chessk.shared

import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.utils.MoveSerializer
import com.techullurgy.chessk.shared.utils.MovesSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class MoveSerializationTest {
    @Test
    fun testMovesSerialization() {
        val temp = Temp(
            availableMoves = listOf(MoveShared(1, 2), MoveShared(3, 5)),
            lastMove = MoveShared(11, 22)
        )

        val encoded = Json.encodeToString(Temp.serializer(), temp)
        assertEquals(
            expected = """{"availableMoves":"1###2***3###5","lastMove":"11###22"}""",
            actual = encoded
        )

        val decoded = Json.decodeFromString(Temp.serializer(), encoded)
        assertEquals(temp, decoded)

        val temp2 = Temp(null, null)
        val encoded2 = Json.encodeToString(Temp.serializer(), temp2)
        assertEquals(
            expected = """{"availableMoves":null,"lastMove":null}""",
            actual = encoded2
        )

        val decoded2 = Json.decodeFromString(Temp.serializer(), encoded2)
        assertEquals(temp2, decoded2)
    }
}

@Serializable
data class Temp(
    @Serializable(with = MovesSerializer::class)
    val availableMoves: List<MoveShared>?,
    @Serializable(with = MoveSerializer::class)
    val lastMove: MoveShared?
)