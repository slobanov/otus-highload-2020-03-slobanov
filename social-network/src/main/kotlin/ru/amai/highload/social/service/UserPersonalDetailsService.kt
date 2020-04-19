package ru.amai.highload.social.service

import ru.amai.highload.social.domain.UserPersonalDetails

interface UserPersonalDetailsService {

    fun getUserPersonalDetailsForUser(login: String): UserPersonalDetails?
    fun setUserPersonalDetails(userPersonalDetails: UserPersonalDetails): UserPersonalDetails
}
