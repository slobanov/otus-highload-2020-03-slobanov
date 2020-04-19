package ru.amai.highload.social.repository

import ru.amai.highload.social.domain.User

interface UserRepository {

    fun findByLogin(login: String): User?
    fun save(user: User): User
}
