package com.techullurgy.chessk.data.database.models

import androidx.room.Entity
import com.techullurgy.chessk.shared.models.PieceColorShared

@Entity(primaryKeys = ["roomId", "name"])
data class MemberEntity(
    val roomId: String,
    val name: String,
    val userId: String,
    val profilePicUrl: String?,
    val assignedColor: PieceColorShared,
    val isOwner: Boolean = false
)