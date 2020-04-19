package ru.amai.highload.social.service

import mu.withLoggingContext
import ru.amai.highload.social.domain.User

interface UserService {

    fun userForLogin(login: String): User?
    fun <T> withUser(login: String, block: User.() -> T): T? =
        withLoggingContext("login" to login) {
            userForLogin(login)?.let { user ->
                withLoggingContext("userId" to user.id.toString()) {
                    block(user)
                }
            }
        }

    fun createNewUser(login: String, password: String): User
}
