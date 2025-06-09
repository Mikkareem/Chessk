package dev.techullurgy.chess.domain

import io.ktor.server.websocket.DefaultWebSocketServerSession
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class GameServer {
    private val userSessions = ConcurrentHashMap<String, DefaultWebSocketServerSession?>()

    private val rooms = ConcurrentHashMap<String, Room>()

    fun createRoom(model: RoomModel): RoomModel {
        val roomId = UUID.randomUUID().toString()
        val room = Room(roomId, model.name, model.description, model.createdBy)
        rooms.put(roomId, room)
        return room.toRoomModel()
    }

    fun deleteRoom(roomId: String) {
        rooms.remove(roomId)
    }

    fun getCreatedRoomsForClientId(clientId: String): List<Room> {
        return rooms.values.filter { it.createdBy == clientId }
    }

    fun createSessionForClientId(clientId: String, session: DefaultWebSocketServerSession) {
        userSessions[clientId] = session
    }

    fun disconnect(clientId: String) {
        getRoomsForClientId(clientId).forEach { it.removePlayer(clientId) }
        userSessions[clientId] = null
    }

    fun getRoomsForClientId(clientId: String): List<Room> {
        return rooms.values.filter { room -> room.getAssignedPlayers().firstOrNull { it.user.clientId == clientId } != null }
    }

    internal fun getRoomById(roomId: String): Room? = rooms[roomId]
    internal fun getSessionForClientId(clientId: String): DefaultWebSocketServerSession? = userSessions[clientId]
}