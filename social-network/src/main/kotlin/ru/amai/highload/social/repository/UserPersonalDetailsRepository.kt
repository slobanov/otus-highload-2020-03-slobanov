package ru.amai.highload.social.repository

import ru.amai.highload.social.domain.UserPersonalDetails

interface UserPersonalDetailsRepository {

    fun findByUserId(userId: Long): UserPersonalDetails?
    fun save(userPersonalDetails: UserPersonalDetails): UserPersonalDetails
    fun findAllOrderById(limit: Int, offset: Long): List<UserPersonalDetails>
    fun findByFirstNamePrefixAndLastNamePrefixOrderById(
        firstNamePrefix: String,
        lastNamePrefix: String,
        limit: Int,
        offset: Long
    ): List<UserPersonalDetails>
}
