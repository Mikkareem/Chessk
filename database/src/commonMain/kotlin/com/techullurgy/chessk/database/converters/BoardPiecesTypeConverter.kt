package com.techullurgy.chessk.database.converters

import androidx.room.TypeConverter
import com.techullurgy.chessk.core.constants.Constants
import com.techullurgy.chessk.core.models.Piece
import com.techullurgy.chessk.core.utils.toBoardPieces
import com.techullurgy.chessk.core.utils.toBoardPiecesString
import com.techullurgy.chessk.database.models.BoardPieces

internal class BoardPiecesTypeConverter {
    @TypeConverter
    fun boardPiecesToString(pieces: BoardPieces?): String? = pieces?.toBoardPiecesString()

    @TypeConverter
    fun stringToBoardPieces(value: String?): BoardPieces? = value?.toBoardPieces()
}