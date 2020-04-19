package ru.amai.highload.social.repository.impl

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.amai.highload.social.domain.City
import ru.amai.highload.social.repository.CityRepository
import ru.amai.highload.social.repository.impl.CityRepositoryImpl.Companion.Column.ID
import ru.amai.highload.social.repository.impl.CityRepositoryImpl.Companion.Column.NAME
import ru.amai.highload.social.repository.impl.CityRepositoryImpl.Companion.Table.CITY

@Repository
class CityRepositoryImpl(
    private val jdbcOperations: NamedParameterJdbcOperations
) : CityRepository {

    override fun findAll(): List<City> =
        jdbcOperations.query(citySql, cityExtractor)

    override fun findByName(name: String): City? =
        jdbcOperations.query("""
            $citySql
            WHERE $CITY.$NAME = :$NAME
        """.trimIndent(),
            mapOf(NAME.name to name),
            cityExtractor
        ).asSingle()

    override fun save(city: City): City {
        val keyHolder = GeneratedKeyHolder()
        jdbcOperations.update(
            insertCitySql,
            MapSqlParameterSource(mapOf(
                NAME.name to city.name
            )),
            keyHolder,
            arrayOf(ID.name)
        )
        val key = keyHolder.key
        requireNotNull(key)

        return city.copy(id = key.toLong())
    }

    private companion object {

        enum class Table {
            CITY
        }

        enum class Column {
            ID,
            NAME
        }

        val citySql = """
            SELECT $CITY.$ID,
                   $CITY.$NAME
              FROM $CITY
        """.trimIndent()

        val insertCitySql = """
            INSERT INTO $CITY (
                $NAME
            ) VALUES (
                :$NAME
            )
        """.trimIndent()

        val cityExtractor = RowMapper { resultSet, _ -> with(resultSet) {
            City(
                id = getLong(ID.name),
                name = getString(NAME.name)
            )
        } }
    }
}
