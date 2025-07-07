package com.techullurgy.chessk.database.converters

import androidx.room.TypeConverter
import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.utils.toMove
import com.techullurgy.chessk.shared.utils.toMoveString

internal class MoveTypeConverter {
    @TypeConverter
    fun moveToString(move: Move?): String? = move?.toMoveString()

    @TypeConverter
    fun stringToMove(value: String?): Move? = value?.toMove()
}