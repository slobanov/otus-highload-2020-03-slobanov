package ru.amai.highload.social.repository

import ru.amai.highload.social.domain.UserPersonalDetails

interface UserPersonalDetailsRepository {

    fun findByUserId(userId: Long): UserPersonalDetails?
    fun save(userPersonalDetails: UserPersonalDetails): UserPersonalDetails
}
