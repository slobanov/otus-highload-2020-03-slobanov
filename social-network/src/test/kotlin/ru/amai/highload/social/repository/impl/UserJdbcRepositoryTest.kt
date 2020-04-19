package ru.amai.highload.social.repository.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import ru.amai.highload.social.domain.User
import ru.amai.highload.social.repository.UserRepository

@DataJdbcTest
@Transactional
@Import(UserJdbcRepository::class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class UserJdbcRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun findByLoginExistsTest() {
        val login = "login1"

        val user = userRepository.findByLogin(login)

        assertThat(user).isNotNull()
        assertThat(user!!.id).`as`("user id").isEqualTo(1L)
        assertThat(user.login).`as`("user login").isEqualTo(login)
        assertThat(user.password).`as`("user password").isEqualTo("password123")
        assertThat(user.roles).`as`("user roles").isEqualTo("USER")
    }

    @Test
    fun findByLoginNotExistsTest() {
        val login = "loginNA"
        assertThat(userRepository.findByLogin(login))
            .`as`("user with login '$login'")
            .isNull()
    }

    @Test
    fun saveTest() {
        val login = "new login"
        val password = "new password"
        val roles = "new roles"
        val user = User(
            login = login,
            password = password,
            roles = roles
        )

        assertThat(userRepository.findByLogin(login))
            .`as`("search result for login '$login' before save")
            .isNull()

        val savedUser = userRepository.save(user)

        assertThat(savedUser.login).`as`("saved user login").isEqualTo(login)
        assertThat(savedUser.password).`as`("saved user password").isEqualTo(password)
        assertThat(savedUser.roles).`as`("saved user roles").isEqualTo(roles)

        assertThat(userRepository.findByLogin(login))
            .`as`("retrieved from repository by login '$login'")
            .isEqualTo(savedUser)
    }
}
