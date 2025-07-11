package com.techullurgy.chessk.data.remote

import com.techullurgy.chessk.data.api.ChessKApi

class RemoteDataSource(
    private val api: ChessKApi
) {
    fun fetchAnyJoinedRoomsAvailable() = api.getJoinedRooms()
}