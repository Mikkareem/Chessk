package com.techullurgy.chessk

import com.techullurgy.chessk.domain.Player
import com.techullurgy.chessk.domain.RoomModel
import com.techullurgy.chessk.domain.toRoomModel
import com.techullurgy.chessk.domain.userRepository
import com.techullurgy.chessk.shared.models.PieceColor
import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import java.io.File

private const val CLIENT_ID_KEY = "client_id"

fun Application.configureRouting() {
    routing {
        uploadProfilePicture()
        createRoom()
        deleteRoom()
        getCreatedRooms()
        joinRoom()
        leaveRoom()
        getJoinedRooms()
        startGame()
    }
}

private fun Route.createRoom() {
    post("/room/create") {
        val model = call.receive<RoomModel>()
        val roomModel = gameServer.createRoom(model)
        call.respond(HttpStatusCode.Accepted, roomModel)
    }
}

private fun Route.deleteRoom() {
    delete("/room/{roomId}") {
        val roomId = call.parameters["roomId"]!!
        gameServer.deleteRoom(roomId)
    }
}

private fun Route.getCreatedRooms() {
    get("/rooms/created") {
        val clientId = call.parameters[CLIENT_ID_KEY]!!
        val createdRooms = gameServer.getCreatedRoomsForClientId(clientId).map { it.toRoomModel() }

        call.respond(HttpStatusCode.OK, createdRooms)
    }
}

private fun Route.joinRoom() {
    post("/room/{roomId}/join") {
        val roomId = call.parameters["roomId"]!!
        val clientId = call.parameters[CLIENT_ID_KEY]!!
        val room = gameServer.getRoomById(roomId)!!

        val user = userRepository.getUserByClientId(clientId)

        if(user == null) {
            call.respond(HttpStatusCode.BadRequest, "No user found")
            return@post
        }

        val assignedPlayers = room.getAssignedPlayers()

        if(assignedPlayers.size >= 2) {
            call.respond(HttpStatusCode.Conflict, "Room is full")
            return@post
        }

        val assignableColor = assignedPlayers
            .takeIf { it.size == 1 }
            ?.let {
                if(it.first().colorAssigned == PieceColor.Black) PieceColor.White else PieceColor.Black
            }


        room.addPlayer(
            Player(
                user = user,
                colorAssigned = assignableColor ?: listOf(PieceColor.White, PieceColor.Black).random(),
                roomId = roomId,
            )
        )

        call.respond(HttpStatusCode.Accepted)
    }
}

private fun Route.leaveRoom() {
    post("/room/{roomId}/leave") {
        val roomId = call.parameters["roomId"]!!
        val clientId = call.parameters[CLIENT_ID_KEY]!!
        val room = gameServer.getRoomById(roomId)!!

        room.removePlayer(clientId)
    }
}

private fun Route.getJoinedRooms() {
    get("/rooms/joined") {
        val clientId = call.parameters[CLIENT_ID_KEY]!!
        val associatedRooms = gameServer.getRoomsForClientId(clientId).map { it.toRoomModel() }

        call.respond(HttpStatusCode.OK, associatedRooms)
    }
}

private fun Route.startGame() {
    post("room/{roomId}/start") {
        val roomId = call.parameters["roomId"]!!
        val room = gameServer.getRoomById(roomId)!!

        room.startGame()
    }
}

private fun Route.uploadProfilePicture() {
    post("user/profile/picture") {
        val multipart = call.receiveMultipart()

        multipart.forEachPart { part ->
            when(part) {
                is PartData.FileItem -> {
                    val filename = part.originalFileName as String
                    val file = File("uploads/$filename")
                    part.provider().copyAndClose(file.writeChannel())
                }
                is PartData.FormItem -> {
                    println("FormItem: ${part.name}")
                }
                else -> TODO()
            }
            part.dispose()
        }

        println("File uploaded successfully")
    }
}