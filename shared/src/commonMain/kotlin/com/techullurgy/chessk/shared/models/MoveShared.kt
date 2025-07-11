package com.techullurgy.chessk.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class MoveShared(
    val from: Int,
    val to: Int
)