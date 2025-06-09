package dev.techullurgy.chess.domain

import dev.techullurgy.chess.events.ReceiverBaseEvent
import dev.techullurgy.chess.events.serializers.receiverBaseEventJson
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

fun List<Piece?>.canPlace(row: Int, column: Int, color: Color): Boolean {
    val index = row rowAndColumn column
    return isEmptyCell(row, column) || this[index]?.pieceColor != color
}


internal fun String.getType(): String? = Json.decodeFromString<JsonObject>(this).getValue("type").jsonPrimitive.content

internal inline fun <reified T> decodeBaseModel(string: String): T = (receiverBaseEventJson.decodeFromString<ReceiverBaseEvent>(string)) as T