package com.techullurgy.chessk.database.models

import androidx.room.Entity
import com.techullurgy.chessk.shared.models.PieceColor

@Entity(primaryKeys = ["roomId", "name"])
data class MemberEntity(
    val roomId: String,
    val name: String,
    val userId: String,
    val profilePicUrl: String,
    val assignedColor: PieceColor,
    val isOwner: Boolean = false
)