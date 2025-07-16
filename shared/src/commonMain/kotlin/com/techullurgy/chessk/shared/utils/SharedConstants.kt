package com.techullurgy.chessk.shared.utils

object SharedConstants {
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

    object Parameters {
        const val CHESSK_CLIENT_ID_HEADER_KEY = "client_id"
    }
}