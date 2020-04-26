package ru.amai.highload.social.repository.impl

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.amai.highload.social.domain.City
import ru.amai.highload.social.domain.Gender
import ru.amai.highload.social.domain.UserPersonalDetails
import ru.amai.highload.social.repository.UserPersonalDetailsRepository
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Column.BIRTH_DATE
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Column.CITY_ID
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Column.CITY_NAME
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Column.FIRST_NAME
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Column.GENDER_ID
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Column.GENDER_NAME
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Column.ID
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Column.LAST_NAME
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Column.NAME
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Column.USER_ID
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Table.CITY
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Table.GENDER
import ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepository.Companion.Table.USER_PERSONAL_DETAILS

@Repository
class UserPersonalDetailsJdbcRepository(
    private val jdbcOperations: NamedParameterJdbcOperations
) : UserPersonalDetailsRepository {

    override fun findByUserId(userId: Long): UserPersonalDetails? =
        findAllByUserIds(listOf(userId)).asSingle()

    override fun findAllByUserIds(userIds: List<Long>): List<UserPersonalDetails> =
        jdbcOperations.query(
            userPersonalDetailsSQL,
            mapOf(USER_ID.name to userIds),
            userPersonalDetailsExtractor
        )

    override fun save(
        userPersonalDetails: UserPersonalDetails
    ): UserPersonalDetails = with(userPersonalDetails) {
        val keyHolder = GeneratedKeyHolder()
        jdbcOperations.update(
            insertUserPersonalDetailsSql,
            MapSqlParameterSource(mapOf(
                USER_ID.name to userId,
                FIRST_NAME.name to firstName,
                LAST_NAME.name to lastName,
                BIRTH_DATE.name to birthDate,
                GENDER_ID.name to gender.id,
                CITY_ID.name to city.id
            )),
            keyHolder,
            arrayOf(ID.name)
        )
        val key = keyHolder.key
        requireNotNull(key)

        return userPersonalDetails.copy(id = key.toLong())
    }

    private companion object {

        enum class Table {
            USER_PERSONAL_DETAILS,
            GENDER,
            CITY
        }

        enum class Column {
            ID,
            NAME,
            USER_ID,
            FIRST_NAME,
            LAST_NAME,
            BIRTH_DATE,
            GENDER_ID,
            GENDER_NAME,
            CITY_ID,
            CITY_NAME,
        }

        val userPersonalDetailsSQL = """
            SELECT $USER_PERSONAL_DETAILS.$ID,
                   $USER_PERSONAL_DETAILS.$USER_ID,
                   $USER_PERSONAL_DETAILS.$FIRST_NAME,
                   $USER_PERSONAL_DETAILS.$LAST_NAME,
                   $USER_PERSONAL_DETAILS.$BIRTH_DATE,
                   $GENDER.$ID AS $GENDER_ID,
                   $GENDER.$NAME AS $GENDER_NAME,
                   $CITY.$ID AS $CITY_ID,
                   $CITY.$NAME AS $CITY_NAME
              FROM $USER_PERSONAL_DETAILS
              JOIN $GENDER
                ON $USER_PERSONAL_DETAILS.$GENDER_ID = $GENDER.$ID
              JOIN $CITY 
                ON $USER_PERSONAL_DETAILS.$CITY_ID = $CITY.$ID
             WHERE $USER_PERSONAL_DETAILS.$USER_ID IN (:$USER_ID)
        """.trimIndent()

        val userPersonalDetailsExtractor = RowMapper { resultSet, _ -> with(resultSet) {
            UserPersonalDetails(
                id = getLong(ID.name),
                userId = getLong(USER_ID.name),
                firstName = getString(FIRST_NAME.name),
                lastName = getString(LAST_NAME.name),
                birthDate = getDate(BIRTH_DATE.name).toLocalDate(),
                gender = Gender(
                    id = getLong(GENDER_ID.name),
                    name = getString(GENDER_NAME.name)
                ),
                city = City(
                    id = getLong(CITY_ID.name),
                    name = getString(CITY_NAME.name)
                )
            )
        } }

        val insertUserPersonalDetailsSql = """
            INSERT INTO $USER_PERSONAL_DETAILS (
                $USER_ID, $FIRST_NAME, $LAST_NAME, $BIRTH_DATE, $GENDER_ID, $CITY_ID
            ) VALUES (
                :$USER_ID, :$FIRST_NAME, :$LAST_NAME, :$BIRTH_DATE, :$GENDER_ID, :$CITY_ID
            )
        """.trimIndent()
    }
}
