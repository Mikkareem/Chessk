package com.techullurgy.chessk.data.database.converters

import androidx.room.TypeConverter
import com.techullurgy.chessk.data.database.models.BoardPieces
import com.techullurgy.chessk.shared.utils.toBoardPieces
import com.techullurgy.chessk.shared.utils.toBoardPiecesString

internal class BoardPiecesTypeConverter {
    @TypeConverter
    fun boardPiecesToString(pieces: BoardPieces?): String? = pieces?.toBoardPiecesString()

    @TypeConverter
    fun stringToBoardPieces(value: String?): BoardPieces? = value?.toBoardPieces()
}