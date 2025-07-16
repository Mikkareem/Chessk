package com.techullurgy.chessk

import com.techullurgy.chessk.domain.userRepository
import com.techullurgy.chessk.shared.utils.SharedConstants
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.netty.EngineMain
import io.ktor.server.request.path
import io.ktor.server.response.respond
import io.ktor.util.logging.KtorSimpleLogger

fun main(args: Array<String>) {
    EngineMain.main(args)
}

internal val LOGGER = KtorSimpleLogger("MyLogging")

fun Application.module() {
    intercept(ApplicationCallPipeline.Call) {
        if (call.request.path().contains("auth/") || call.request.path()
                .contains("/test")
        ) return@intercept

        val clientId = call.parameters[SharedConstants.Parameters.CHESSK_CLIENT_ID_HEADER_KEY]
        if (clientId == null) {
            call.respond(HttpStatusCode.Forbidden, "Client Id is mandatory")
            return@intercept
        }

        val foundUser = userRepository.getUserByClientId(clientId)
        if (foundUser == null) {
            call.respond(HttpStatusCode.Forbidden, "Invalid Client Id")
            return@intercept
        }
    }

    configureSerialization()
    configureSockets()
    configureRouting()
}