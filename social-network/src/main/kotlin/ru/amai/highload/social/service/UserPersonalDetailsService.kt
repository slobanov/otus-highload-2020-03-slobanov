package ru.amai.highload.social.service

import ru.amai.highload.social.domain.UserPersonalDetails
import ru.amai.highload.social.domain.UserPersonalDetailsLight

interface UserPersonalDetailsService {

    fun getUserPersonalDetailsForUser(login: String): UserPersonalDetails?
    fun searchUserPersonalDetailsLight(
        limit: Int,
        offset: Long,
        searchSpeck: UserPersonalDetailsSearchSpeck
    ): List<UserPersonalDetailsLight>
    fun setUserPersonalDetails(userPersonalDetails: UserPersonalDetails): UserPersonalDetails
}
