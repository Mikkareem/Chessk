package com.techullurgy.chessk.domain

data class User(
    val clientId: String,
    val userId: String,
    val userName: String,
    val email: String,
    val password: String,
    val profilePicUrl: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (clientId != other.clientId) return false
        if (userId != other.userId) return false
        if (userName != other.userName) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clientId.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + userName.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }
}