package dev.techullurgy.chess.events

import dev.techullurgy.chess.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_TIMER_UPDATE)
data class TimerUpdate(
    val roomId: String,
    val whiteTime: Long,
    val blackTime: Long,
): SenderBaseEvent