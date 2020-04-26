package ru.amai.highload.social.service

import ru.amai.highload.social.domain.UserPersonalDetails
import ru.amai.highload.social.domain.UserPersonalDetailsLight

interface UserPersonalDetailsService {

    fun getUserPersonalDetailsForUser(login: String): UserPersonalDetails?
    fun getAllUserPersonalDetailsLight(limit: Int, offset: Long): List<UserPersonalDetailsLight>
    fun setUserPersonalDetails(userPersonalDetails: UserPersonalDetails): UserPersonalDetails
}
