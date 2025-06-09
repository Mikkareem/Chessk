package dev.techullurgy.chess.events

import dev.techullurgy.chess.domain.Color
import dev.techullurgy.chess.domain.Member
import dev.techullurgy.chess.domain.Player
import dev.techullurgy.chess.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_GAME_STARTED)
data class GameStarted(
    val roomId: String,
    val members: List<Member>,
    val assignedColor: Color? = null
): SenderBaseEvent