package com.techullurgy.chessk.data.api

import com.techullurgy.chessk.base.AppResult
import com.techullurgy.chessk.shared.dto.AuthLoginRequest
import com.techullurgy.chessk.shared.dto.AuthRegisterRequest
import com.techullurgy.chessk.shared.dto.AuthSuccessResponse
import com.techullurgy.chessk.shared.dto.CreateRoomRequest
import com.techullurgy.chessk.shared.dto.CreateRoomResponse
import com.techullurgy.chessk.shared.dto.GameRoomResponse
import com.techullurgy.chessk.shared.dto.JoinRoomRequest
import com.techullurgy.chessk.shared.dto.JoinRoomResponse
import kotlinx.coroutines.flow.Flow

interface ChessKApi {
    fun registerUser(body: AuthRegisterRequest): Flow<AppResult<AuthSuccessResponse>>

    fun loginUser(body: AuthLoginRequest): Flow<AppResult<AuthSuccessResponse>>

    fun createRoom(body: CreateRoomRequest): Flow<AppResult<CreateRoomResponse>>

    fun joinRoom(body: JoinRoomRequest): Flow<AppResult<JoinRoomResponse>>

    fun getCreatedRooms(): Flow<AppResult<List<GameRoomResponse>>>

    fun getJoinedRooms(): Flow<AppResult<List<GameRoomResponse>>>

    fun startGame(roomId: String): Flow<AppResult<Unit>>

    fun leaveRoom(roomId: String): Flow<AppResult<Unit>>

    suspend fun connectGameWebsocket(): WebsocketSession
}