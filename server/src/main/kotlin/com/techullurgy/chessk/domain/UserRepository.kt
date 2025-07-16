package com.techullurgy.chessk.domain

import io.ktor.util.collections.ConcurrentSet

interface UserRepository {
    fun getUserByClientId(clientId: String): User?
    fun getUserByEmail(email: String): User?

    fun saveUser(user: User)
}

val userRepository = object : UserRepository {
    private val users = ConcurrentSet<User>()

    override fun getUserByEmail(email: String): User? = users.find { it.email == email }

    override fun getUserByClientId(clientId: String): User? = users.find { it.clientId == clientId }

    override fun saveUser(user: User) {
        users.add(user)
    }
}