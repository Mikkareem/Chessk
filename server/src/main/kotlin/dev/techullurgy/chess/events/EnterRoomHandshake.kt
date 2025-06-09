package dev.techullurgy.chess.events

import dev.techullurgy.chess.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_ENTER_ROOM_HANDSHAKE)
data class EnterRoomHandshake(
    val roomId: String,
): ReceiverBaseEvent
