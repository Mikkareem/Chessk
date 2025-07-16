package com.techullurgy.chessk.feature.game.models

sealed interface Piece {
    val color: PieceColor

    data class Rook(override val color: PieceColor) : Piece
    data class Bishop(override val color: PieceColor) : Piece
    data class Knight(override val color: PieceColor) : Piece
    data class Queen(override val color: PieceColor) : Piece
    data class King(override val color: PieceColor) : Piece
    data class Pawn(override val color: PieceColor) : Piece
}