package com.techullurgy.chessk.core.remote

import com.techullurgy.chessk.core.constants.Constants
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.parameter
import io.ktor.util.generateNonce

internal val clientId = generateNonce()

internal val ClientIdIntercept = createClientPlugin("ClientIdIntercept") {
    onRequest { request, _ ->
        request.parameter(
            Constants.HttpClientHeadersConstants.CLIENT_ID,
            clientId
        )
    }
}