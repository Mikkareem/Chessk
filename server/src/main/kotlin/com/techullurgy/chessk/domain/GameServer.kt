package com.techullurgy.chessk.domain

import com.techullurgy.chessk.shared.models.GameRoom
import io.ktor.server.websocket.DefaultWebSocketServerSession
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class GameServer {
    private val userSessions = ConcurrentHashMap<String, DefaultWebSocketServerSession?>()

    private val rooms = ConcurrentHashMap<String, Room>()

    fun createRoom(model: GameRoom): GameRoom {
        val roomId = UUID.randomUUID().toString()
        val room = Room(roomId, model.roomName, model.roomDescription, model.createdBy)
        rooms.put(roomId, room)
        return room.toGameRoom()
    }

    fun deleteRoom(roomId: String) {
        rooms.remove(roomId)?.invalidateRoom()
    }

    fun getCreatedRoomsForUserId(userId: String): List<Room> {
        return rooms.values.filter { it.createdBy == userId }
    }

    fun createSessionForClientId(clientId: String, session: DefaultWebSocketServerSession) {
        userSessions[clientId] = session
    }

    fun disconnect(clientId: String) {
        userSessions[clientId] = null
    }

    fun getJoinedRoomsForClientId(clientId: String): List<Room> {
        return rooms.values.filter { room -> room.getAssignedPlayers().firstOrNull { it.user.clientId == clientId } != null }
    }

    internal fun getRoomById(roomId: String): Room? = rooms[roomId]
    internal fun getSessionForClientId(clientId: String): DefaultWebSocketServerSession? = userSessions[clientId]
}