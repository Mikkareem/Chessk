package dev.techullurgy.chess.domain

data class User(
    val clientId: String,
    val userId: String,
    val userName: String,
    val profilePicUrl: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (clientId != other.clientId) return false
        if (userId != other.userId) return false
        if (userName != other.userName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clientId.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + userName.hashCode()
        return result
    }
}