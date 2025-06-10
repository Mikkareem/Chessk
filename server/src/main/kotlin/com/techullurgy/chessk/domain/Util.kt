package com.techullurgy.chessk.domain

import com.techullurgy.chessk.shared.events.ClientToServerBaseEvent
import com.techullurgy.chessk.shared.events.clientToServerBaseEventJson
import com.techullurgy.chessk.shared.models.PieceColor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

val Int.row get() = this / 8
val Int.column get() = this % 8

infix fun Int.rowAndColumn(other: Int) = this * 8 + other

fun List<Piece?>.isEmptyCell(row: Int, column: Int): Boolean {
    val index = row rowAndColumn column
    return this[index] == null
}

fun List<Piece?>.canPlace(row: Int, column: Int, color: PieceColor): Boolean {
    val index = row rowAndColumn column
    return isEmptyCell(row, column) || this[index]?.pieceColor != color
}

internal fun String.getType(): String? = Json.decodeFromString<JsonObject>(this).getValue("type").jsonPrimitive.content

internal inline fun <reified T> decodeBaseModel(string: String): T = (clientToServerBaseEventJson.decodeFromString<ClientToServerBaseEvent>(string)) as T