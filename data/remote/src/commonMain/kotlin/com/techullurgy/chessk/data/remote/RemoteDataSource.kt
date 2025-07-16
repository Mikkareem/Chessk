package com.techullurgy.chessk.data.remote

import com.techullurgy.chessk.data.api.ChessKApi
import com.techullurgy.chessk.shared.dto.AuthLoginRequest
import com.techullurgy.chessk.shared.dto.AuthRegisterRequest
import com.techullurgy.chessk.shared.dto.CreateRoomRequest
import com.techullurgy.chessk.shared.dto.JoinRoomRequest

class RemoteDataSource(
    private val api: ChessKApi
) {
    fun fetchAnyJoinedRoomsAvailable() = api.getJoinedRooms()

    fun getCreatedRooms() = api.getCreatedRooms()

    fun create(req: CreateRoomRequest) = api.createRoom(req)

    fun joinRoom(req: JoinRoomRequest) = api.joinRoom(req)

    fun loginUser(req: AuthLoginRequest) = api.loginUser(req)

    fun registerUser(req: AuthRegisterRequest) = api.registerUser(req)

    fun startGame(roomId: String) = api.startGame(roomId)

    fun leaveRoom(roomId: String) = api.leaveRoom(roomId)

    fun uploadProfilePicture(bytes: ByteArray) = api.uploadProfilePicture(bytes)
}