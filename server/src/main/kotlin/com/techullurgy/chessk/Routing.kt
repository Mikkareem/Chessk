package com.techullurgy.chessk

import com.techullurgy.chessk.domain.Player
import com.techullurgy.chessk.domain.User
import com.techullurgy.chessk.domain.toGameRoom
import com.techullurgy.chessk.domain.userRepository
import com.techullurgy.chessk.shared.dto.AuthLoginRequest
import com.techullurgy.chessk.shared.dto.AuthRegisterRequest
import com.techullurgy.chessk.shared.dto.AuthSuccessResponse
import com.techullurgy.chessk.shared.dto.CreateRoomRequest
import com.techullurgy.chessk.shared.dto.CreateRoomResponse
import com.techullurgy.chessk.shared.dto.GameRoomResponse
import com.techullurgy.chessk.shared.dto.JoinRoomRequest
import com.techullurgy.chessk.shared.dto.JoinRoomResponse
import com.techullurgy.chessk.shared.endpoints.CreateRoomEndpoint
import com.techullurgy.chessk.shared.endpoints.DeleteRoomEndpoint
import com.techullurgy.chessk.shared.endpoints.GetCreatedRoomsEndpoint
import com.techullurgy.chessk.shared.endpoints.GetJoinedRoomsEndpoint
import com.techullurgy.chessk.shared.endpoints.JoinRoomEndpoint
import com.techullurgy.chessk.shared.endpoints.LeaveRoomEndpoint
import com.techullurgy.chessk.shared.endpoints.LoginUserEndpoint
import com.techullurgy.chessk.shared.endpoints.RegisterUserEndpoint
import com.techullurgy.chessk.shared.endpoints.StartGameEndpoint
import com.techullurgy.chessk.shared.endpoints.UploadProfilePictureEndpoint
import com.techullurgy.chessk.shared.models.MemberShared
import com.techullurgy.chessk.shared.models.PieceColorShared
import com.techullurgy.chessk.shared.utils.SharedConstants
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import java.io.File
import java.util.UUID

fun Application.configureRouting() {
    routing {
        registerUser()
        loginUser()
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
    post(CreateRoomEndpoint.signature) {
        val clientId = call.parameters[SharedConstants.Parameters.CHESSK_CLIENT_ID_HEADER_KEY]!!
        val createdBy = userRepository.getUserByClientId(clientId)?.userId ?: ""
        val model = call.receive<CreateRoomRequest>().room.copy(createdBy = createdBy)
        val roomModel = gameServer.createRoom(model)
        call.respond(HttpStatusCode.Accepted, CreateRoomResponse(roomModel))
    }
}

private fun Route.deleteRoom() {
    delete(DeleteRoomEndpoint.signature) {
        val roomId = call.parameters[DeleteRoomEndpoint.PARAM_ROOM_ID]!!
        gameServer.deleteRoom(roomId)
    }
}

private fun Route.getCreatedRooms() {
    get(GetCreatedRoomsEndpoint.signature) {
        val clientId = call.parameters[SharedConstants.Parameters.CHESSK_CLIENT_ID_HEADER_KEY]!!
        val userId = userRepository.getUserByClientId(clientId)!!.userId
        val createdRooms = gameServer.getCreatedRoomsForUserId(userId).map {
            val joinedUsers = it.getAssignedPlayers().map { player ->
                MemberShared(
                    roomId = player.roomId,
                    name = player.user.userName,
                    assignedColor = player.colorAssigned,
                    userId = player.user.userId,
                    profilePicUrl = player.user.profilePicUrl,
                    isOwner = player.user.clientId == clientId
                )
            }
            GameRoomResponse(
                room = it.toGameRoom(),
                joinedUsers = joinedUsers
            )
        }

        call.respond(HttpStatusCode.OK, createdRooms)
    }
}

private fun Route.joinRoom() {
    post(JoinRoomEndpoint.signature) {
        val request = call.receive<JoinRoomRequest>()
        val roomId = request.roomId
        val clientId = call.parameters[SharedConstants.Parameters.CHESSK_CLIENT_ID_HEADER_KEY]!!
        val room = gameServer.getRoomById(roomId)!!

        val user = userRepository.getUserByClientId(clientId)!!

        val assignedPlayers = room.getAssignedPlayers()

        if(assignedPlayers.size >= 2) {
            call.respond(HttpStatusCode.Conflict, "Room is full")
            return@post
        }

        val assignableColor = assignedPlayers
            .takeIf { it.size == 1 }
            ?.let {
                if (it.first().colorAssigned == PieceColorShared.Black) PieceColorShared.White else PieceColorShared.Black
            }

        val assignedColor = assignableColor ?: request.preferredColor

        room.addPlayer(
            Player(
                user = user,
                colorAssigned = assignedColor,
                roomId = roomId,
            )
        )

        call.respond(HttpStatusCode.Accepted, JoinRoomResponse(roomId, assignedColor))
    }
}

private fun Route.leaveRoom() {
    post(LeaveRoomEndpoint.signature) {
        val roomId = call.parameters[LeaveRoomEndpoint.PARAM_ROOM_ID]!!
        val clientId = call.parameters[SharedConstants.Parameters.CHESSK_CLIENT_ID_HEADER_KEY]!!
        val room = gameServer.getRoomById(roomId)!!

        room.removePlayer(clientId)
        call.respond(HttpStatusCode.OK, Unit)
    }
}

private fun Route.getJoinedRooms() {
    get(GetJoinedRoomsEndpoint.signature) {
        val clientId = call.parameters[SharedConstants.Parameters.CHESSK_CLIENT_ID_HEADER_KEY]!!
        val associatedRooms = gameServer.getJoinedRoomsForClientId(clientId).map {
            val joinedUsers = it.getAssignedPlayers().map { player ->
                MemberShared(
                    roomId = player.roomId,
                    name = player.user.userName,
                    assignedColor = player.colorAssigned,
                    userId = player.user.userId,
                    profilePicUrl = player.user.profilePicUrl,
                    isOwner = player.user.clientId == clientId
                )
            }
            GameRoomResponse(
                room = it.toGameRoom(),
                joinedUsers = joinedUsers
            )
        }

        call.respond(HttpStatusCode.OK, associatedRooms)
    }
}

private fun Route.startGame() {
    post(StartGameEndpoint.signature) {
        val roomId = call.parameters[StartGameEndpoint.PARAM_ROOM_ID]!!
        val room = gameServer.getRoomById(roomId)!!

        room.startGame()
        call.respond(HttpStatusCode.OK, Unit)
    }
}

private fun Route.registerUser() {
    post(RegisterUserEndpoint.signature) {
        val authUser = call.receive<AuthRegisterRequest>()
        val clientId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val user = User(
            clientId = clientId,
            userId = userId,
            userName = authUser.name,
            email = authUser.email,
            password = authUser.password,
        )
        userRepository.saveUser(user)
        call.respond(HttpStatusCode.OK, AuthSuccessResponse(user.clientId))
    }
}

private fun Route.loginUser() {
    post(LoginUserEndpoint.signature) {
        val authUser = call.receive<AuthLoginRequest>()
        val user = userRepository.getUserByEmail(authUser.email)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "User Not found")
            return@post
        }

        if (authUser.password != user.password) {
            call.respond(HttpStatusCode.Unauthorized, "Credentials not match")
            return@post
        }

        call.respond(HttpStatusCode.OK, AuthSuccessResponse(user.clientId))
    }
}

private fun Route.uploadProfilePicture() {
    post(UploadProfilePictureEndpoint.signature) {
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