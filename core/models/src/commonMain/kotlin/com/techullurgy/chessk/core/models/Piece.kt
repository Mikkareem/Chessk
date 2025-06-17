package com.techullurgy.chessk.core.models

import com.techullurgy.chessk.core.constants.Constants
import com.techullurgy.chessk.shared.models.PieceColor

sealed class Piece {
    abstract val color: PieceColor

    protected abstract fun encodePiece(): String

    fun encodeAsString(): String {
        val colorCode = when(color) {
            PieceColor.White -> Constants.Codes.CHESSK_WHITE_COLOR_CODE
            PieceColor.Black -> Constants.Codes.CHESSK_BLACK_COLOR_CODE
        }
        return "$colorCode${encodePiece()}"
    }

    companion object {
        fun decodeFromString(value: String): Piece? {
            if(value.length != 2) return null

            val color = when(value.first()) {
                Constants.Codes.CHESSK_WHITE_COLOR_CODE -> PieceColor.White
                Constants.Codes.CHESSK_BLACK_COLOR_CODE -> PieceColor.Black
                else -> return null
            }

            return when(value.last()) {
                Constants.Codes.CHESSK_BISHOP_CODE -> Bishop(color)
                Constants.Codes.CHESSK_KING_CODE -> King(color)
                Constants.Codes.CHESSK_KNIGHT_CODE -> Knight(color)
                Constants.Codes.CHESSK_PAWN_CODE -> Pawn(color)
                Constants.Codes.CHESSK_QUEEN_CODE -> Queen(color)
                Constants.Codes.CHESSK_ROOK_CODE -> Rook(color)
                else -> null
            }
        }
    }
}

data class King(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${Constants.Codes.CHESSK_KING_CODE}"
    }
}

data class Queen(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${Constants.Codes.CHESSK_QUEEN_CODE}"
    }
}

data class Bishop(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${Constants.Codes.CHESSK_BISHOP_CODE}"
    }
}

data class Knight(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${Constants.Codes.CHESSK_KNIGHT_CODE}"
    }
}

data class Rook(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${Constants.Codes.CHESSK_ROOK_CODE}"
    }
}

data class Pawn(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${Constants.Codes.CHESSK_PAWN_CODE}"
    }
}