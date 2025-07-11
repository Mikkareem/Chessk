package com.techullurgy.chessk.feature.game.data.mappers

import com.techullurgy.chessk.feature.game.models.GameRoom
import com.techullurgy.chessk.feature.game.models.Member
import com.techullurgy.chessk.feature.game.models.Move
import com.techullurgy.chessk.feature.game.models.Piece
import com.techullurgy.chessk.feature.game.models.PieceColor
import com.techullurgy.chessk.shared.models.Bishop
import com.techullurgy.chessk.shared.models.GameRoomShared
import com.techullurgy.chessk.shared.models.King
import com.techullurgy.chessk.shared.models.Knight
import com.techullurgy.chessk.shared.models.MemberShared
import com.techullurgy.chessk.shared.models.MoveShared
import com.techullurgy.chessk.shared.models.Pawn
import com.techullurgy.chessk.shared.models.PieceColorShared
import com.techullurgy.chessk.shared.models.PieceShared
import com.techullurgy.chessk.shared.models.Queen
import com.techullurgy.chessk.shared.models.Rook

internal fun PieceColorShared.toModel(): PieceColor = when (this) {
    PieceColorShared.Black -> PieceColor.Black
    PieceColorShared.White -> PieceColor.White
}

internal fun PieceColor.toShared(): PieceColorShared = when (this) {
    PieceColor.Black -> PieceColorShared.Black
    PieceColor.White -> PieceColorShared.White
}

internal fun PieceShared.toModel(): Piece = when (this) {
    is Bishop -> Piece.Bishop(color.toModel())
    is King -> Piece.King(color.toModel())
    is Knight -> Piece.Knight(color.toModel())
    is Pawn -> Piece.Pawn(color.toModel())
    is Queen -> Piece.Queen(color.toModel())
    is Rook -> Piece.Rook(color.toModel())
}

internal fun Piece.toShared(): PieceShared = when (this) {
    is Piece.Bishop -> Bishop(color.toShared())
    is Piece.King -> King(color.toShared())
    is Piece.Knight -> Knight(color.toShared())
    is Piece.Pawn -> Pawn(color.toShared())
    is Piece.Queen -> Queen(color.toShared())
    is Piece.Rook -> Rook(color.toShared())
}

internal fun MoveShared.toModel() = Move(from, to)
internal fun Move.toShared() = MoveShared(from, to)

internal fun MemberShared.toModel() = Member(
    name = name,
    assignedColor = assignedColor.toModel(),
    userId = userId,
    profilePicUrl = profilePicUrl
)

internal fun Member.toShared() = MemberShared(
    name = name,
    assignedColor = assignedColor.toShared(),
    userId = userId,
    profilePicUrl = profilePicUrl
)

internal fun GameRoomShared.toModel() = GameRoom(
    roomId = roomId,
    roomName = roomName,
    roomDescription = roomDescription,
    createdBy = createdBy,
)

internal fun GameRoom.toShared() = GameRoomShared(
    roomId = roomId,
    roomName = roomName,
    roomDescription = roomDescription,
    createdBy = createdBy
)