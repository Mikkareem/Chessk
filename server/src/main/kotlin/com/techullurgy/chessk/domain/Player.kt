package com.techullurgy.chessk.domain

import com.techullurgy.chessk.gameServer
import com.techullurgy.chessk.shared.events.ServerToClientBaseEvent
import com.techullurgy.chessk.shared.models.PieceColor
import io.ktor.server.websocket.sendSerialized
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class Player(
    val user: User,
    val colorAssigned: PieceColor,
    val roomId: String,
) {
    var timeLeft: Duration = 30.minutes

    fun sendEvent(event: ServerToClientBaseEvent) {
        gameServer.getSessionForClientId(user.clientId)?.let {
            if(it.isActive) {
                it.launch {
                    it.sendSerialized<ServerToClientBaseEvent>(event)
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Player) return false

        if (user != other.user) return false

        return true
    }

    override fun hashCode(): Int {
        return user.hashCode()
    }
}