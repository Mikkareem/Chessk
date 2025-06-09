package dev.techullurgy.chess.domain

import dev.techullurgy.chess.events.SenderBaseEvent
import dev.techullurgy.chess.events.serializers.senderBaseEventJson
import dev.techullurgy.chess.gameServer
import io.ktor.websocket.send
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class Player(
    val user: User,
    val colorAssigned: Color,
    val roomId: String,
) {
    var timeLeft: Duration = 30.minutes

    fun sendEvent(event: SenderBaseEvent) {
        gameServer.getSessionForClientId(user.clientId)?.let {
            val message = senderBaseEventJson.encodeToString<SenderBaseEvent>(event)
            if(it.isActive) {
                it.launch {
                    it.send(message)
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