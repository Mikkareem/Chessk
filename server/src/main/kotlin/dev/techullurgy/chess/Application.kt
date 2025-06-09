package dev.techullurgy.chess

import dev.techullurgy.chess.sessions.GameSession
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.util.generateNonce
import kotlinx.serialization.Serializable

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    install(Sessions) {
        cookie<GameSession>("SESSION")
    }

    intercept(ApplicationCallPipeline.Call) {
        if(call.sessions.get<GameSession>() == null) {
            val clientId = call.parameters["client_id"]!!
            call.sessions.set(GameSession(clientId, generateNonce()))
        }
    }

    configureSerialization()
    configureSockets()
    configureRouting()
}