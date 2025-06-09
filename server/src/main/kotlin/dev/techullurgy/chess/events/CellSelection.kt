package dev.techullurgy.chess.events

import dev.techullurgy.chess.domain.Color
import dev.techullurgy.chess.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_CELL_SELECTION)
data class CellSelection(
    val roomId: String,
    val color: Color,
    val selectedIndex: Int
): ReceiverBaseEvent