package ru.amai.highload.social.service

import ru.amai.highload.social.domain.Interest

interface InterestService {

    fun interestsForUser(login: String): List<Interest>
    fun allInterests(): List<Interest>
    fun addInterestsForUser(userId: Long, interestNames: List<String>): List<Interest>
}
