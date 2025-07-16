package com.techullurgy.chessk.shared.utils

import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.PieceShared
import kotlinx.serialization.json.Json

fun String.toBoardPieces(): List<PieceShared?> = Json.decodeFromString(BoardSerializer, "\"$this\"")

fun String.toCutPieces(): Set<PieceShared>? =
    Json.decodeFromString(CutPiecesSerializer, "\"$this\"")

fun String.toMove(): MoveShared? = Json.decodeFromString(MoveSerializer, "\"$this\"")

fun String.toMoves(): List<MoveShared>? = Json.decodeFromString(MovesSerializer, "\"$this\"")

fun List<PieceShared?>.toBoardPiecesString(): String =
    Json.encodeToString(BoardSerializer, this).removeSurrounding("\"")

fun Set<PieceShared>.toCutPiecesString(): String =
    Json.encodeToString(CutPiecesSerializer, this).removeSurrounding("\"")

fun MoveShared.toMoveString(): String =
    Json.encodeToString(MoveSerializer, this).removeSurrounding("\"")

fun List<MoveShared>.toMovesString(): String =
    Json.encodeToString(MovesSerializer, this).removeSurrounding("\"")
