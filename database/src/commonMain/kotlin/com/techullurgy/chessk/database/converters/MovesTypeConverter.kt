package com.techullurgy.chessk.database.converters

import androidx.room.TypeConverter
import com.techullurgy.chessk.core.constants.Constants
import com.techullurgy.chessk.core.utils.toMoves
import com.techullurgy.chessk.core.utils.toMovesString
import com.techullurgy.chessk.shared.models.Move

internal class MovesTypeConverter {
    @TypeConverter
    fun movesToString(moves: List<Move>?): String? = moves?.toMovesString()

    @TypeConverter
    fun stringToMoves(value: String?): List<Move>? = value?.toMoves()
}