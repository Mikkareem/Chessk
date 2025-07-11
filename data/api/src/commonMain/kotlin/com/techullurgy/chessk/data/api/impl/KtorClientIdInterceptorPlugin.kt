package com.techullurgy.chessk.data.api.impl

import com.techullurgy.chessk.data.datastore.DatastoreManager
import com.techullurgy.chessk.shared.utils.SharedConstants
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object KtorClientIdInterceptorPlugin : KoinComponent {

    private val manager: DatastoreManager by inject()

    val plugin = createClientPlugin("ClientIdIntercept") {
        onRequest { request, _ ->
            manager.clientIdFlow.first()?.let { clientId ->
                request.parameter(SharedConstants.Parameters.CHESSK_CLIENT_ID_HEADER_KEY, clientId)
            }
        }
    }
}