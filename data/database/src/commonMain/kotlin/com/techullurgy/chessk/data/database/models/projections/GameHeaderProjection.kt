package com.techullurgy.chessk.data.database.models.projections

import androidx.room.Embedded
import androidx.room.Relation
import com.techullurgy.chessk.data.database.models.GameEntity
import com.techullurgy.chessk.data.database.models.MemberEntity
import com.techullurgy.chessk.data.database.models.TimerEntity

data class GameHeaderProjection(
    val roomId: String,
    val roomName: String,
    val membersCount: Int,
    val isMyTurn: Boolean,
    val yourTime: Long,
    val opponentTime: Long
)

data class GameWithMembersAndTimer(
    @Embedded
    val game: GameEntity,

    @Relation(
        parentColumn = "roomId",
        entityColumn = "roomId"
    )
    val members: List<MemberEntity>,

    @Relation(
        parentColumn = "roomId",
        entityColumn = "roomId"
    )
    val timer: TimerEntity?
)