package com.techullurgy.chessk.core.constants

import com.techullurgy.chessk.shared.utils.SharedConstants

object Constants {

    object Core {
        const val CHESSK_PRIMARY_DELIMITER = "***"
        const val CHESSK_SECONDARY_DELIMITER = "###"
        const val CHESSK_NULL_REPLACEMENT = "."
    }

    object Codes {
        const val CHESSK_WHITE_COLOR_CODE = 'W'
        const val CHESSK_BLACK_COLOR_CODE = 'B'

        const val CHESSK_KING_CODE = 'K'
        const val CHESSK_QUEEN_CODE = 'Q'
        const val CHESSK_BISHOP_CODE = 'B'
        const val CHESSK_KNIGHT_CODE = 'N'
        const val CHESSK_ROOK_CODE = 'R'
        const val CHESSK_PAWN_CODE = 'P'
    }

    object HttpClientHeadersConstants {
        const val CLIENT_ID = SharedConstants.Parameters.CHESSK_CLIENT_ID_HEADER_KEY
    }

    object KoinQualifierNamedConstants {
        const val APP_COROUTINE_SCOPE = "APP_COROUTINE_SCOPE"
        const val WEBSOCKET_HTTP_CLIENT = "WEBSOCKET_HTTP_CLIENT"
        const val AUTH_HTTP_CLIENT = "AUTH_HTTP_CLIENT"
    }
}