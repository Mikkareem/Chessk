package com.techullurgy.chessk.shared.endpoints

sealed class Endpoint {
    protected abstract val routeVariables: Array<String>

    protected open fun buildUrl(): String = signature

    val signature: String
        get() = routeVariables.joinToString("/")

    val actualUrl: String get() = buildUrl()
}

data object RegisterUserEndpoint : Endpoint() {
    override val routeVariables: Array<String>
        get() = arrayOf("auth", "register")
}

data object LoginUserEndpoint : Endpoint() {
    override val routeVariables: Array<String>
        get() = arrayOf("auth", "login")
}

data object GameWebsocketEndpoint : Endpoint() {
    override val routeVariables: Array<String>
        get() = arrayOf("join", "ws")
}

data object CreateRoomEndpoint : Endpoint() {
    override val routeVariables: Array<String>
        get() = arrayOf("room", "create")
}

data object JoinRoomEndpoint : Endpoint() {
    override val routeVariables: Array<String>
        get() = arrayOf("room", "join")
}

data object GetCreatedRoomsEndpoint : Endpoint() {
    override val routeVariables: Array<String>
        get() = arrayOf("rooms", "created")
}

data object GetJoinedRoomsEndpoint : Endpoint() {
    override val routeVariables: Array<String>
        get() = arrayOf("rooms", "joined")
}

data object UploadProfilePictureEndpoint : Endpoint() {
    override val routeVariables: Array<String>
        get() = arrayOf("user", "profile", "picture")
}

data class DeleteRoomEndpoint(
    private val roomId: String
) : Endpoint() {
    override fun buildUrl(): String {
        return signature.replace("{roomId}", roomId)
    }

    override val routeVariables: Array<String>
        get() = arrayOf("room", "{roomId}")

    companion object {
        val signature = DeleteRoomEndpoint("").signature
        const val PARAM_ROOM_ID = "roomId"
    }
}

data class LeaveRoomEndpoint(
    private val roomId: String
) : Endpoint() {
    override fun buildUrl(): String {
        return signature.replace("{roomId}", roomId)
    }

    override val routeVariables: Array<String>
        get() = arrayOf("room", "{roomId}", "leave")

    companion object {
        val signature = LeaveRoomEndpoint("").signature
        const val PARAM_ROOM_ID = "roomId"
    }
}

data class StartGameEndpoint(
    private val roomId: String
) : Endpoint() {
    override fun buildUrl(): String {
        return signature.replace("{roomId}", roomId)
    }

    override val routeVariables: Array<String>
        get() = arrayOf("room", "{roomId}", "start")

    companion object {
        val signature = StartGameEndpoint("").signature
        const val PARAM_ROOM_ID = "roomId"
    }
}