package com.techullurgy.chessk.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimerEntity(
    @PrimaryKey val roomId: String,
    val whiteTime: Long,
    val blackTime: Long
)