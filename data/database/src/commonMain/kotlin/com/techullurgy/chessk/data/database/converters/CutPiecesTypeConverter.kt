package com.techullurgy.chessk.data.database.converters

import androidx.room.TypeConverter
import com.techullurgy.chessk.database.models.CutPieces
import com.techullurgy.chessk.shared.utils.toCutPieces
import com.techullurgy.chessk.shared.utils.toCutPiecesString

internal class CutPiecesTypeConverter {
    @TypeConverter
    fun cutPiecesToString(pieces: CutPieces?): String? = pieces?.toCutPiecesString()

    @TypeConverter
    fun stringToCutPieces(value: String?): CutPieces? = value?.toCutPieces()
}