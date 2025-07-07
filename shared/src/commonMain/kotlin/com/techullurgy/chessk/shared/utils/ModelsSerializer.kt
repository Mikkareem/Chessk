package com.techullurgy.chessk.shared.utils

import com.techullurgy.chessk.shared.models.Move
import com.techullurgy.chessk.shared.models.Piece
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

private fun Collection<Piece?>.encodeAsPiecesString(): String {
    return joinToString(SharedConstants.Core.CHESSK_PRIMARY_DELIMITER) {
        when (it) {
            null -> SharedConstants.Core.CHESSK_NULL_REPLACEMENT
            else -> it.encodeAsString()
        }
    }
}

private fun String.decodePieces(): Collection<Piece?> {
    return split(SharedConstants.Core.CHESSK_PRIMARY_DELIMITER).map {
        when (it) {
            SharedConstants.Core.CHESSK_NULL_REPLACEMENT -> null
            else -> Piece.decodeFromString(it)
        }
    }
}

private fun Collection<Move>.encodeAsMovesString(): String {
    val primaryDelimiter = SharedConstants.Core.CHESSK_PRIMARY_DELIMITER
    val secondaryDelimiter = SharedConstants.Core.CHESSK_SECONDARY_DELIMITER

    return joinToString(primaryDelimiter) { "${it.from}$secondaryDelimiter${it.to}" }
}

private fun String.decodeMoves(): Collection<Move> {
    val primaryDelimiter = SharedConstants.Core.CHESSK_PRIMARY_DELIMITER
    val secondaryDelimiter = SharedConstants.Core.CHESSK_SECONDARY_DELIMITER

    return split(primaryDelimiter).map {
        val from = it.substringBefore(secondaryDelimiter).toInt()
        val to = it.substringAfter(secondaryDelimiter).toInt()
        Move(from, to)
    }
}

private fun Move.encodeAsString(): String {
    val delimiter = SharedConstants.Core.CHESSK_SECONDARY_DELIMITER
    return let { "${it.from}$delimiter${it.to}" }
}

private fun String.decodeMove(): Move {
    val delimiter = SharedConstants.Core.CHESSK_SECONDARY_DELIMITER
    return let {
        val from = it.substringBefore(delimiter).toInt()
        val to = it.substringAfter(delimiter).toInt()
        Move(from, to)
    }
}

private object MovePrimitiveSerializer : KSerializer<Move> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("PiecesList", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Move
    ) {
        val encoded = value.encodeAsString()
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): Move {
        val value = decoder.decodeString()
        return value.decodeMove()
    }
}

private object MoveListSerializer : KSerializer<List<Move>> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("PiecesList", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: List<Move>
    ) {
        val encoded = value.encodeAsMovesString()
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): List<Move> {
        val value = decoder.decodeString()
        return value.decodeMoves().toList()
    }
}

private object NullablePieceListSerializer : KSerializer<List<Piece?>> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("PiecesList", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: List<Piece?>
    ) {
        val encoded = value.encodeAsPiecesString()
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): List<Piece?> {
        val value = decoder.decodeString()
        return value.decodePieces().toList()
    }
}

private object PieceSetSerializer : KSerializer<Set<Piece>> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("PiecesList", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Set<Piece>
    ) {
        val encoded = value.encodeAsPiecesString()
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): Set<Piece> {
        val value = decoder.decodeString()
        return value.decodePieces().filterNotNull().toSet()
    }
}

object BoardSerializer : KSerializer<List<Piece?>> by NullablePieceListSerializer
object CutPiecesSerializer : KSerializer<Set<Piece>?> by PieceSetSerializer.nullable
object MovesSerializer : KSerializer<List<Move>?> by MoveListSerializer.nullable
object MoveSerializer : KSerializer<Move?> by MovePrimitiveSerializer.nullable