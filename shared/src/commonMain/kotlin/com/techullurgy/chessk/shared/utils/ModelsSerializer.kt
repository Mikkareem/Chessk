package com.techullurgy.chessk.shared.utils

import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.PieceShared
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

private fun Collection<PieceShared?>.encodeAsPiecesString(): String {
    return joinToString(SharedConstants.Core.CHESSK_PRIMARY_DELIMITER) {
        when (it) {
            null -> SharedConstants.Core.CHESSK_NULL_REPLACEMENT
            else -> it.encodeAsString()
        }
    }
}

private fun String.decodePieces(): Collection<PieceShared?> {
    return split(SharedConstants.Core.CHESSK_PRIMARY_DELIMITER).map {
        when (it) {
            SharedConstants.Core.CHESSK_NULL_REPLACEMENT -> null
            else -> PieceShared.decodeFromString(it)
        }
    }
}

private fun Collection<MoveShared>.encodeAsMovesString(): String {
    val primaryDelimiter = SharedConstants.Core.CHESSK_PRIMARY_DELIMITER
    val secondaryDelimiter = SharedConstants.Core.CHESSK_SECONDARY_DELIMITER

    return joinToString(primaryDelimiter) { "${it.from}$secondaryDelimiter${it.to}" }
}

private fun String.decodeMoves(): Collection<MoveShared> {
    val primaryDelimiter = SharedConstants.Core.CHESSK_PRIMARY_DELIMITER
    val secondaryDelimiter = SharedConstants.Core.CHESSK_SECONDARY_DELIMITER

    return split(primaryDelimiter).map {
        val from = it.substringBefore(secondaryDelimiter).toInt()
        val to = it.substringAfter(secondaryDelimiter).toInt()
        MoveShared(from, to)
    }
}

private fun MoveShared.encodeAsString(): String {
    val delimiter = SharedConstants.Core.CHESSK_SECONDARY_DELIMITER
    return let { "${it.from}$delimiter${it.to}" }
}

private fun String.decodeMove(): MoveShared {
    val delimiter = SharedConstants.Core.CHESSK_SECONDARY_DELIMITER
    return let {
        val from = it.substringBefore(delimiter).toInt()
        val to = it.substringAfter(delimiter).toInt()
        MoveShared(from, to)
    }
}

private object MovePrimitiveSerializer : KSerializer<MoveShared> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("PiecesList", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: MoveShared
    ) {
        val encoded = value.encodeAsString()
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): MoveShared {
        val value = decoder.decodeString()
        return value.decodeMove()
    }
}

private object MoveListSerializer : KSerializer<List<MoveShared>> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("PiecesList", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: List<MoveShared>
    ) {
        val encoded = value.encodeAsMovesString()
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): List<MoveShared> {
        val value = decoder.decodeString()
        return value.decodeMoves().toList()
    }
}

private object NullablePieceListSerializer : KSerializer<List<PieceShared?>> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("PiecesList", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: List<PieceShared?>
    ) {
        val encoded = value.encodeAsPiecesString()
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): List<PieceShared?> {
        val value = decoder.decodeString()
        return value.decodePieces().toList()
    }
}

private object PieceSetSerializer : KSerializer<Set<PieceShared>> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("PiecesList", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Set<PieceShared>
    ) {
        val encoded = value.encodeAsPiecesString()
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): Set<PieceShared> {
        val value = decoder.decodeString()
        return value.decodePieces().filterNotNull().toSet()
    }
}

object BoardSerializer : KSerializer<List<PieceShared?>> by NullablePieceListSerializer
object CutPiecesSerializer : KSerializer<Set<PieceShared>?> by PieceSetSerializer.nullable
object MovesSerializer : KSerializer<List<MoveShared>?> by MoveListSerializer.nullable
object MoveSerializer : KSerializer<MoveShared?> by MovePrimitiveSerializer.nullable