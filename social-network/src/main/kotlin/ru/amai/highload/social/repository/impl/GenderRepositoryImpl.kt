package ru.amai.highload.social.repository.impl

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import ru.amai.highload.social.domain.Gender
import ru.amai.highload.social.repository.GenderRepository
import ru.amai.highload.social.repository.impl.GenderRepositoryImpl.Companion.Column.ID
import ru.amai.highload.social.repository.impl.GenderRepositoryImpl.Companion.Column.NAME
import ru.amai.highload.social.repository.impl.GenderRepositoryImpl.Companion.Table.GENDER

@Repository
class GenderRepositoryImpl(
    private val jdbcOperations: NamedParameterJdbcOperations
) : GenderRepository {

    override fun findAll(): List<Gender> =
        jdbcOperations.query(genderSql, genderExtractor)

    override fun findByName(name: String): Gender? =
        jdbcOperations.query("""
            $genderSql
            WHERE $GENDER.$NAME = :$NAME
        """.trimIndent(),
            mapOf(NAME.name to name),
            genderExtractor
        ).asSingle()

    private companion object {

        enum class Table {
            GENDER
        }

        enum class Column {
            ID,
            NAME
        }

        val genderSql = """
            SELECT $GENDER.$ID,
                   $GENDER.$NAME
              FROM $GENDER
        """.trimIndent()

        val genderExtractor = RowMapper { resultSet, _ -> with(resultSet) {
            Gender(
                id = getLong(ID.name),
                name = getString(NAME.name)
            )
        } }
    }
}
