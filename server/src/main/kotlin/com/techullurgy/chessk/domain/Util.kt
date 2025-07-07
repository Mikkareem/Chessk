package com.techullurgy.chessk.domain

import com.techullurgy.chessk.shared.models.PieceColor

val Int.row get() = this / 8
val Int.column get() = this % 8

infix fun Int.rowAndColumn(other: Int) = this * 8 + other

fun List<GamePiece?>.isEmptyCell(row: Int, column: Int): Boolean {
    val index = row rowAndColumn column
    return this[index] == null
}

fun List<GamePiece?>.canPlace(row: Int, column: Int, color: PieceColor): Boolean {
    val index = row rowAndColumn column
    return isEmptyCell(row, column) || this[index]?.pieceColor != color
}