package ru.amai.highload.social.service

import ru.amai.highload.social.domain.Following

interface FollowerService {

    fun listFollowedBy(login: String): List<String>
    fun canFollow(currentLogin: String, userToFollowLogin: String): Boolean
    fun follow(currentLogin: String, userToFollowLogin: String): Following
}
