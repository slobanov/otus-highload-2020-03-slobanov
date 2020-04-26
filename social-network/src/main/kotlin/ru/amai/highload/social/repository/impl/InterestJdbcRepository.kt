package ru.amai.highload.social.repository.impl

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import ru.amai.highload.social.domain.Interest
import ru.amai.highload.social.repository.InterestRepository
import ru.amai.highload.social.repository.impl.InterestJdbcRepository.Companion.Column.ID
import ru.amai.highload.social.repository.impl.InterestJdbcRepository.Companion.Column.INTEREST_ID
import ru.amai.highload.social.repository.impl.InterestJdbcRepository.Companion.Column.NAME
import ru.amai.highload.social.repository.impl.InterestJdbcRepository.Companion.Column.USER_ID
import ru.amai.highload.social.repository.impl.InterestJdbcRepository.Companion.Table.INTEREST
import ru.amai.highload.social.repository.impl.InterestJdbcRepository.Companion.Table.USER_TO_INTEREST

@Repository
class InterestJdbcRepository(
    private val jdbcOperations: NamedParameterJdbcOperations
) : InterestRepository {

    override fun findByUserId(userId: Long): List<Interest> =
        jdbcOperations.query(
            userInterestsSQL,
            mapOf(USER_ID.name to userId),
            interestExtractor
        )

    override fun findAll(): List<Interest> =
        jdbcOperations.query(
            interestsSQL,
            interestExtractor
        )

    override fun mergeAll(interests: List<Interest>): List<Interest> {
        val interestNames = interests.map(Interest::name)
        jdbcOperations.batchUpdate(
            mergeInterestSql,
            interestNames.map { mapOf(NAME.name to it) }.toTypedArray()
        )
        return findByNames(interestNames)
    }

    override fun setUserId(userId: Long, interests: List<Interest>) {
        val userIdValue = USER_ID.name to userId
        jdbcOperations.batchUpdate(
            insertUserToInterestSql,
            interests.map { mapOf(
                userIdValue,
                INTEREST_ID.name to it.id
            ) }.toTypedArray()
        )
    }

    private fun findByNames(names: List<String>): List<Interest> =
        jdbcOperations.query("""
            $interestsSQL
            WHERE $INTEREST.$NAME IN (:$NAME)
        """.trimIndent(),
            mapOf(NAME.name to names),
            interestExtractor
        )

    private companion object {
        enum class Table {
            USER_TO_INTEREST,
            INTEREST
        }

        enum class Column {
            ID,
            INTEREST_ID,
            NAME,
            USER_ID
        }

        val interestsSQL = """
            SELECT $INTEREST.$ID,
                   $INTEREST.$NAME
              FROM $INTEREST
        """.trimIndent()

        val userInterestsSQL = """
            $interestsSQL
              JOIN $USER_TO_INTEREST
                ON $USER_TO_INTEREST.$INTEREST_ID = $INTEREST.$ID
             WHERE $USER_TO_INTEREST.$USER_ID = :$USER_ID 
        """.trimIndent()

        val mergeInterestSql = """
            INSERT INTO $INTEREST (
                $NAME
            ) VALUES (
                :$NAME
            ) ON DUPLICATE KEY UPDATE
                $NAME = :$NAME
        """.trimIndent()

        val insertUserToInterestSql = """
            INSERT INTO $USER_TO_INTEREST (
                $USER_ID, $INTEREST_ID
            ) VALUES (
                :$USER_ID, :$INTEREST_ID
            )
        """.trimIndent()

        val interestExtractor = RowMapper { resultSet, _ -> with(resultSet) {
            Interest(
                id = getLong(ID.name),
                name = getString(NAME.name)
            )
        } }
    }
}
