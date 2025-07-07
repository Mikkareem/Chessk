package com.techullurgy.chessk.feature.game.data.utils

import com.techullurgy.chessk.shared.models.Bishop
import com.techullurgy.chessk.shared.models.King
import com.techullurgy.chessk.shared.models.Knight
import com.techullurgy.chessk.shared.models.Pawn
import com.techullurgy.chessk.shared.models.PieceColor
import com.techullurgy.chessk.shared.models.Queen
import com.techullurgy.chessk.shared.models.Rook

val sampleInitialBoard = List(64) {
    when(it) {
        0, 7 -> Rook(PieceColor.White)
        1, 6 -> Knight(PieceColor.White)
        2, 5 -> Bishop(PieceColor.White)
        3 -> King(PieceColor.White)
        4 -> Queen(PieceColor.White)
        in 8..15 -> Pawn(PieceColor.White)
        in 48..55 -> Pawn(PieceColor.Black)
        56, 63 -> Rook(PieceColor.Black)
        57, 62 -> Knight(PieceColor.Black)
        58, 61 -> Bishop(PieceColor.Black)
        59 -> Queen(PieceColor.Black)
        60 -> King(PieceColor.Black)
        else -> null
    }
}