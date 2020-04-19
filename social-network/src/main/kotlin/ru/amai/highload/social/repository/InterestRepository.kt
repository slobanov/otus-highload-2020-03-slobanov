package ru.amai.highload.social.repository

import ru.amai.highload.social.domain.Interest

interface InterestRepository {

    fun findByUserId(userId: Long): List<Interest>
    fun findAll(): List<Interest>
    fun mergeAll(interests: List<Interest>): List<Interest>
    fun setUserId(userId: Long, interests: List<Interest>)
}
