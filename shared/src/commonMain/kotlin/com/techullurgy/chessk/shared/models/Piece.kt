package com.techullurgy.chessk.shared.models

import com.techullurgy.chessk.shared.utils.SharedConstants
import kotlinx.serialization.Serializable

@Serializable
sealed class Piece {
    abstract val color: PieceColor

    protected abstract fun encodePiece(): String

    fun encodeAsString(): String {
        val colorCode = when(color) {
            PieceColor.White -> SharedConstants.Codes.CHESSK_WHITE_COLOR_CODE
            PieceColor.Black -> SharedConstants.Codes.CHESSK_BLACK_COLOR_CODE
        }
        return "$colorCode${encodePiece()}"
    }

    override final fun toString(): String = encodeAsString()

    companion object {
        fun decodeFromString(value: String): Piece? {
            if(value.length != 2) return null

            val color = when(value.first()) {
                SharedConstants.Codes.CHESSK_WHITE_COLOR_CODE -> PieceColor.White
                SharedConstants.Codes.CHESSK_BLACK_COLOR_CODE -> PieceColor.Black
                else -> return null
            }

            return when(value.last()) {
                SharedConstants.Codes.CHESSK_BISHOP_CODE -> Bishop(color)
                SharedConstants.Codes.CHESSK_KING_CODE -> King(color)
                SharedConstants.Codes.CHESSK_KNIGHT_CODE -> Knight(color)
                SharedConstants.Codes.CHESSK_PAWN_CODE -> Pawn(color)
                SharedConstants.Codes.CHESSK_QUEEN_CODE -> Queen(color)
                SharedConstants.Codes.CHESSK_ROOK_CODE -> Rook(color)
                else -> null
            }
        }
    }
}

@Serializable
data class King(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_KING_CODE}"
    }
}

@Serializable
data class Queen(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_QUEEN_CODE}"
    }
}

@Serializable
data class Bishop(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_BISHOP_CODE}"
    }
}

@Serializable
data class Knight(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_KNIGHT_CODE}"
    }
}

@Serializable
data class Rook(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_ROOK_CODE}"
    }
}

@Serializable
data class Pawn(override val color: PieceColor): Piece() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_PAWN_CODE}"
    }
}