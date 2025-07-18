package com.techullurgy.chessk.shared.events

object BaseEventConstants {
    // SERVER EVENTS
    const val TYPE_GAME_STARTED = "TYPE_GAME_STARTED"
    const val TYPE_SELECTION_RESULT = "TYPE_SELECTION_RESULT"
    const val TYPE_RESET_SELECTION_DONE = "TYPE_RESET_SELECTION_DONE"
    const val TYPE_GAME_UPDATE = "TYPE_GAME_UPDATE"
    const val TYPE_TIMER_UPDATE = "TYPE_TIMER_UPDATE"

    // CLIENT EVENTS
    const val TYPE_ENTER_ROOM_HANDSHAKE = "TYPE_ENTER_ROOM_HANDSHAKE"
    const val TYPE_CELL_SELECTION = "TYPE_CELL_SELECTION"
    const val TYPE_PIECE_MOVE = "TYPE_PIECE_MOVE"
    const val TYPE_RESET_SELECTION = "TYPE_RESET_SELECTION"
    const val TYPE_DISCONNECT = "TYPE_DISCONNECT"
}