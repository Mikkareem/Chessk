package com.techullurgy.chessk.data.database.converters

import androidx.room.TypeConverter
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.utils.toMove
import com.techullurgy.chessk.shared.utils.toMoveString

internal class MoveTypeConverter {
    @TypeConverter
    fun moveToString(move: MoveShared?): String? = move?.toMoveString()

    @TypeConverter
    fun stringToMove(value: String?): MoveShared? = value?.toMove()
}