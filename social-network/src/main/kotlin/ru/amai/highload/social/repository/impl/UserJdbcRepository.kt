package ru.amai.highload.social.repository.impl

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.amai.highload.social.domain.User
import ru.amai.highload.social.repository.UserRepository
import ru.amai.highload.social.repository.impl.UserJdbcRepository.Companion.Column.ID
import ru.amai.highload.social.repository.impl.UserJdbcRepository.Companion.Column.LOGIN
import ru.amai.highload.social.repository.impl.UserJdbcRepository.Companion.Column.PASSWORD
import ru.amai.highload.social.repository.impl.UserJdbcRepository.Companion.Column.ROLES
import ru.amai.highload.social.repository.impl.UserJdbcRepository.Companion.Table.USER

@Repository
class UserJdbcRepository(
    private val jdbcOperations: NamedParameterJdbcOperations
) : UserRepository {

    override fun findByLogin(login: String): User? =
        jdbcOperations.query("""
            $userSql
            WHERE $USER.$LOGIN = :$LOGIN
        """.trimIndent(),
            mapOf(LOGIN.name to login),
            userExtractor
        ).asSingle()

    override fun save(user: User): User = with(user) {
        val keyHolder = GeneratedKeyHolder()
        jdbcOperations.update(
            insertUserSql,
            MapSqlParameterSource(mapOf(
                LOGIN.name to login,
                PASSWORD.name to password,
                ROLES.name to roles
            )),
            keyHolder,
            arrayOf(ID.name)
        )
        val key = keyHolder.key
        requireNotNull(key)

        return user.copy(id = key.toLong())
    }

    override fun findByIds(userIds: List<Long>): List<User> =
        jdbcOperations.query("""
            $userSql
            WHERE $USER.$ID in (:$ID)
        """.trimIndent(),
            mapOf(ID.name to userIds),
            userExtractor
        )

    private companion object {
        enum class Table {
            USER
        }

        enum class Column {
            ID,
            LOGIN,
            PASSWORD,
            ROLES
        }

        val userSql = """
            SELECT $USER.$ID,
                   $USER.$LOGIN,
                   $USER.$PASSWORD,
                   $USER.$ROLES
              FROM $USER
        """.trimIndent()

        val insertUserSql = """
            INSERT INTO $USER (
                $LOGIN, $PASSWORD, $ROLES
            ) VALUES (
                :$LOGIN, :$PASSWORD, :$ROLES
            )
        """.trimIndent()

        val userExtractor = RowMapper { resultSet, _ -> with(resultSet) {
            User(
                id = getLong(ID.name),
                login = getString(LOGIN.name),
                password = getString(PASSWORD.name),
                roles = getString(ROLES.name)
            )
        } }
    }
}
