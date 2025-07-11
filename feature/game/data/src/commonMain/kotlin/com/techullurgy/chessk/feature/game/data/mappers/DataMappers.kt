package com.techullurgy.chessk.feature.game.data.mappers

import com.techullurgy.chessk.data.database.models.GameEntity
import com.techullurgy.chessk.data.database.models.MemberEntity
import com.techullurgy.chessk.feature.game.models.GameRoom
import com.techullurgy.chessk.feature.game.models.Member
import com.techullurgy.chessk.shared.dto.GameRoomResponse

context(game: GameEntity, members: List<MemberEntity>)
internal fun asGameRoom(): GameRoom {
    val gameRoom = game.toGameRoom()
    return gameRoom.copy(members = members.map { it.toMember() })
}

internal fun GameRoomResponse.toMemberEntities(): List<MemberEntity> = joinedUsers.map {
    MemberEntity(
        roomId = room.roomId,
        name = it.name,
        userId = it.userId,
        profilePicUrl = it.profilePicUrl,
        assignedColor = it.assignedColor,
        isOwner = it.userId == room.createdBy
    )
}

internal fun GameRoomResponse.toGameEntity(): GameEntity = GameEntity(
    roomId = room.roomId,
    roomName = room.roomName,
    roomDescription = room.roomDescription,
    createdBy = room.createdBy
)

private fun MemberEntity.toMember(): Member = Member(
    name = name,
    assignedColor = assignedColor.toModel(),
    userId = userId,
    profilePicUrl = profilePicUrl
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