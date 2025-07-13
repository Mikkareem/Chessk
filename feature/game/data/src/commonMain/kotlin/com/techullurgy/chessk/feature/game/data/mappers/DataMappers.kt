package com.techullurgy.chessk.feature.game.data.mappers

import com.techullurgy.chessk.data.database.models.GameEntity
import com.techullurgy.chessk.data.database.models.MemberEntity
import com.techullurgy.chessk.data.database.models.TimerEntity
import com.techullurgy.chessk.feature.game.models.GameRoom
import com.techullurgy.chessk.feature.game.models.Member
import com.techullurgy.chessk.feature.game.models.PieceColor
import com.techullurgy.chessk.shared.dto.GameRoomResponse
import com.techullurgy.chessk.shared.models.MemberShared

context(game: GameEntity?, members: List<MemberEntity>, timer: TimerEntity?)
internal fun asGameRoom(): GameRoom? {
    if (game == null) return null
    var gameRoom = game.toGameRoom()

    val yourTime = timer?.let {
        if (gameRoom.assignedColor == PieceColor.White) timer.whiteTime else timer.blackTime
    } ?: 0L

    val opponentTime = timer?.let {
        if (gameRoom.assignedColor == PieceColor.White) timer.blackTime else timer.whiteTime
    } ?: 0L

    gameRoom = gameRoom.copy(yourTime = yourTime, opponentTime = opponentTime)

    return gameRoom.copy(members = members.map { it.toMember() })
}

internal fun GameRoomResponse.toMemberEntities(): List<MemberEntity> =
    joinedUsers.map { it.toEntity() }

internal fun GameRoomResponse.toGameEntity(): GameEntity = GameEntity(
    roomId = room.roomId,
    roomName = room.roomName,
    roomDescription = room.roomDescription,
    createdBy = room.createdBy
)

internal fun MemberShared.toEntity() = MemberEntity(
    roomId = roomId,
    name = name,
    userId = userId,
    profilePicUrl = profilePicUrl,
    assignedColor = assignedColor,
    isOwner = isOwner
)

private fun MemberEntity.toMember(): Member = Member(
    roomId = roomId,
    name = name,
    assignedColor = assignedColor.toModel(),
    userId = userId,
    profilePicUrl = profilePicUrl,
    isOwner = isOwner
)

private fun GameEntity.toGameRoom() = GameRoom(
    roomId = roomId,
    roomName = roomName,
    roomDescription = roomDescription,
    createdBy = createdBy,
    board = board.map { it?.toModel() },
    assignedColor = assignedColor?.toModel(),
    currentPlayer = currentPlayer?.toModel(),
    cutPieces = cutPieces?.map { it.toModel() }?.toSet(),
    availableMoves = availableMoves?.map { it.toModel() },
    lastMove = lastMove?.toModel(),
    selectedIndex = selectedIndex,
    kingInCheckIndex = kingInCheckIndex,
)