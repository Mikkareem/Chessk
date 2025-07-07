package com.techullurgy.chessk.database.converters

import androidx.room.TypeConverter
import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.utils.toMoves
import com.techullurgy.chessk.shared.utils.toMovesString

internal class MovesTypeConverter {
    @TypeConverter
    fun movesToString(moves: List<Move>?): String? = moves?.toMovesString()

    @TypeConverter
    fun stringToMoves(value: String?): List<Move>? = value?.toMoves()
}