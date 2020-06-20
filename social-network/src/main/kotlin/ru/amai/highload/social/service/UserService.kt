package ru.amai.highload.social.service

import mu.withLoggingContext
import ru.amai.highload.social.domain.User

interface UserService {

    fun <T> withUser(login: String, block: User.() -> T): T? =
        withLoggingContext("login" to login) {
            userForLogin(login)?.let { user ->
                requireNotNull(user.id)
                withLoggingContext("userId" to user.id.toString()) {
                    block(user)
                }
            }
        }

    fun userForLogin(login: String): User?
    fun createNewUser(login: String, password: String): User
    fun listUsersByIds(userIds: List<Long>): List<User>
}
