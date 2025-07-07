package com.techullurgy.chessk.shared.utils

import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.models.Piece
import kotlinx.serialization.json.Json

fun String.toBoardPieces(): List<Piece?> = Json.decodeFromString(BoardSerializer, "\"$this\"")

fun String.toCutPieces(): Set<Piece>? = Json.decodeFromString(CutPiecesSerializer, "\"$this\"")

fun String.toMove(): Move? = Json.decodeFromString(MoveSerializer, "\"$this\"")

fun String.toMoves(): List<Move>? = Json.decodeFromString(MovesSerializer, "\"$this\"")

fun List<Piece?>.toBoardPiecesString(): String =
    Json.encodeToString(BoardSerializer, this).removeSurrounding("\"")

fun Set<Piece>.toCutPiecesString(): String =
    Json.encodeToString(CutPiecesSerializer, this).removeSurrounding("\"")

fun Move.toMoveString(): String = Json.encodeToString(MoveSerializer, this).removeSurrounding("\"")

fun List<Move>.toMovesString(): String =
    Json.encodeToString(MovesSerializer, this).removeSurrounding("\"")
