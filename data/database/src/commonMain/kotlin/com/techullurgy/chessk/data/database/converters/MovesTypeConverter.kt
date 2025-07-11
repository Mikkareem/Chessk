package com.techullurgy.chessk.data.database.converters

import androidx.room.TypeConverter
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.utils.toMoves
import com.techullurgy.chessk.shared.utils.toMovesString

internal class MovesTypeConverter {
    @TypeConverter
    fun movesToString(moves: List<MoveShared>?): String? = moves?.toMovesString()

    @TypeConverter
    fun stringToMoves(value: String?): List<MoveShared>? = value?.toMoves()
}