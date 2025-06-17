package com.techullurgy.chessk.core.utils

import com.techullurgy.chessk.core.constants.Constants
import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.core.models.Piece

fun String.toBoardPieces(): List<Piece?> {
    val primaryDelimiter = Constants.Core.CHESSK_PRIMARY_DELIMITER
    val nullReplacement = Constants.Core.CHESSK_NULL_REPLACEMENT

    return split(primaryDelimiter).map {
        if(it == nullReplacement) return@map null

        Piece.decodeFromString(it)
    }
}

fun String.toCutPieces(): Set<Piece> {
    val primaryDelimiter = Constants.Core.CHESSK_PRIMARY_DELIMITER

    return split(primaryDelimiter).mapNotNull {
        Piece.decodeFromString(it)
    }.toSet()
}

fun List<Piece?>.toBoardPiecesString(): String {
    val primaryDelimiter = Constants.Core.CHESSK_PRIMARY_DELIMITER
    val nullReplacement = Constants.Core.CHESSK_NULL_REPLACEMENT

    return joinToString(primaryDelimiter) {
        it?.encodeAsString() ?: nullReplacement
    }
}

fun Set<Piece>.toCutPiecesString(): String {
    val primaryDelimiter = Constants.Core.CHESSK_PRIMARY_DELIMITER

    return joinToString(primaryDelimiter) { it.encodeAsString() }
}

fun String.toMove(): Move {
    val delimiter = Constants.Core.CHESSK_PRIMARY_DELIMITER
    return let {
        val from = it.substringBefore(delimiter).toInt()
        val to = it.substringAfter(delimiter).toInt()
        Move(from, to)
    }
}

fun Move.toMoveString(): String {
    val delimiter = Constants.Core.CHESSK_PRIMARY_DELIMITER
    return let { "${it.from}$delimiter${it.to}" }
}

fun List<Move>.toMovesString(): String {
    val primaryDelimiter = Constants.Core.CHESSK_PRIMARY_DELIMITER
    val secondaryDelimiter = Constants.Core.CHESSK_SECONDARY_DELIMITER

    return joinToString(secondaryDelimiter) { "${it.from}$primaryDelimiter${it.to}" }
}

fun String.toMoves(): List<Move> {
    val primaryDelimiter = Constants.Core.CHESSK_PRIMARY_DELIMITER
    val secondaryDelimiter = Constants.Core.CHESSK_SECONDARY_DELIMITER

    return split(secondaryDelimiter).map {
        val from = it.substringBefore(primaryDelimiter).toInt()
        val to = it.substringAfter(primaryDelimiter).toInt()
        Move(from, to)
    }
}