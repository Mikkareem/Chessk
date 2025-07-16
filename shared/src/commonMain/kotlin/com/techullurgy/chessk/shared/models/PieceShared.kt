package com.techullurgy.chessk.shared.models

import com.techullurgy.chessk.shared.utils.SharedConstants
import kotlinx.serialization.Serializable

@Serializable
sealed class PieceShared {
    abstract val color: PieceColorShared

    protected abstract fun encodePiece(): String

    fun encodeAsString(): String {
        val colorCode = when(color) {
            PieceColorShared.White -> SharedConstants.Codes.CHESSK_WHITE_COLOR_CODE
            PieceColorShared.Black -> SharedConstants.Codes.CHESSK_BLACK_COLOR_CODE
        }
        return "$colorCode${encodePiece()}"
    }

    final override fun toString(): String = encodeAsString()

    companion object Companion {
        fun decodeFromString(value: String): PieceShared? {
            if(value.length != 2) return null

            val color = when(value.first()) {
                SharedConstants.Codes.CHESSK_WHITE_COLOR_CODE -> PieceColorShared.White
                SharedConstants.Codes.CHESSK_BLACK_COLOR_CODE -> PieceColorShared.Black
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
data class King(override val color: PieceColorShared) : PieceShared() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_KING_CODE}"
    }
}

@Serializable
data class Queen(override val color: PieceColorShared) : PieceShared() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_QUEEN_CODE}"
    }
}

@Serializable
data class Bishop(override val color: PieceColorShared) : PieceShared() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_BISHOP_CODE}"
    }
}

@Serializable
data class Knight(override val color: PieceColorShared) : PieceShared() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_KNIGHT_CODE}"
    }
}

@Serializable
data class Rook(override val color: PieceColorShared) : PieceShared() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_ROOK_CODE}"
    }
}

@Serializable
data class Pawn(override val color: PieceColorShared) : PieceShared() {
    override fun encodePiece(): String {
        return "${SharedConstants.Codes.CHESSK_PAWN_CODE}"
    }
}