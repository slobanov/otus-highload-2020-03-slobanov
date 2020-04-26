package ru.amai.highload.social.repository.impl

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import ru.amai.highload.social.domain.Following
import ru.amai.highload.social.repository.FollowerRepository
import ru.amai.highload.social.repository.impl.FollowerJdbcRepository.Companion.Column.FOLLOWED_ID
import ru.amai.highload.social.repository.impl.FollowerJdbcRepository.Companion.Column.FOLLOWER_ID
import ru.amai.highload.social.repository.impl.FollowerJdbcRepository.Companion.Table.FOLLOWER

@Repository
class FollowerJdbcRepository(
    private val jdbcOperations: NamedParameterJdbcOperations
) : FollowerRepository {

    override fun findFollowedBy(userId: Long): List<Following> =
        jdbcOperations.query(
            followedSql,
            mapOf(FOLLOWER_ID.name to userId),
            followingExtractor
        )

    override fun findByFollowerAndFollowerId(followerId: Long, followedId: Long): Following? =
        jdbcOperations.query("""
            $followedSql
            AND $FOLLOWER.$FOLLOWED_ID = :$FOLLOWED_ID
        """.trimIndent(),
            mapOf(
                FOLLOWER_ID.name to followerId,
                FOLLOWED_ID.name to followedId
            ),
            followingExtractor
        ).asSingle()

    override fun save(following: Following): Following {
        jdbcOperations.update(
            insertFollowingSql,
            MapSqlParameterSource(mapOf(
                FOLLOWER_ID.name to following.followerId,
                FOLLOWED_ID.name to following.followedId
            ))
        )
        return following
    }

    private companion object {

        enum class Table {
            FOLLOWER,
        }

        enum class Column {
            FOLLOWED_ID,
            FOLLOWER_ID
        }

        val followedSql = """
            SELECT $FOLLOWER.$FOLLOWER_ID,
                   $FOLLOWER.$FOLLOWED_ID
              FROM $FOLLOWER
             WHERE $FOLLOWER.$FOLLOWER_ID = :$FOLLOWER_ID
        """.trimIndent()

        val insertFollowingSql = """
            INSERT INTO $FOLLOWER (
                $FOLLOWER_ID, $FOLLOWED_ID
            ) VALUES (
                :$FOLLOWER_ID, :$FOLLOWED_ID
            )
        """.trimIndent()

        val followingExtractor = RowMapper { resultSet, _ -> with(resultSet) {
            Following(
                followerId = getLong(FOLLOWER_ID.name),
                followedId = getLong(FOLLOWED_ID.name)
            )
        } }
    }
}
