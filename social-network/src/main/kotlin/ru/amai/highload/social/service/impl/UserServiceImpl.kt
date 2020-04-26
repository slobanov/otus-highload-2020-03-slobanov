package ru.amai.highload.social.service.impl

import mu.KLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.amai.highload.social.domain.User
import ru.amai.highload.social.repository.UserRepository
import ru.amai.highload.social.service.UserService

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun userForLogin(login: String): User? {
        val user = userRepository.findByLogin(login)
        if (user == null) { logger.info { "Failed to find user by login" } }
        return user
    }

    override fun createNewUser(login: String, password: String): User {
        require(userRepository.findByLogin(login) == null) {
            "User with login '$login' already exists"
        }
        return userRepository.save(
            User(
                login = login,
                password = passwordEncoder.encode(password),
                roles = USER_ROLE
            )
        )
    }

    override fun listUsers(limit: Int, offset: Long): List<User> =
        userRepository.findAll(limit, offset)

    override fun listUsersByIds(userIds: List<Long>): List<User> =
        userRepository.findByIds(userIds)

    private companion object : KLogging() {
        const val USER_ROLE = "USER"
    }
}
