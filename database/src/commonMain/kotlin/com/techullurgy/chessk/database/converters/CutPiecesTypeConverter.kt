package com.techullurgy.chessk.database.converters

import androidx.room.TypeConverter
import com.techullurgy.chessk.core.constants.Constants
import com.techullurgy.chessk.core.models.Piece
import com.techullurgy.chessk.core.utils.toCutPieces
import com.techullurgy.chessk.core.utils.toCutPiecesString
import com.techullurgy.chessk.database.models.CutPieces

internal class CutPiecesTypeConverter {
    @TypeConverter
    fun cutPiecesToString(pieces: CutPieces?): String? = pieces?.toCutPiecesString()

    @TypeConverter
    fun stringToCutPieces(value: String?): CutPieces? = value?.toCutPieces()
}