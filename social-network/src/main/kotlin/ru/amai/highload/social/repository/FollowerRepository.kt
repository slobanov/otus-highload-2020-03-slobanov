package ru.amai.highload.social.repository

import ru.amai.highload.social.domain.Following

interface FollowerRepository {

    fun findFollowedBy(userId: Long): List<Following>
    fun findByFollowerAndFollowerId(followerId: Long, followedId: Long): Following?
    fun save(following: Following): Following
}
