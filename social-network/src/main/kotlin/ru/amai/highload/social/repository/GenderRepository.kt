package ru.amai.highload.social.repository

import ru.amai.highload.social.domain.Gender

interface GenderRepository {

    fun findAll(): List<Gender>
    fun findByName(name: String): Gender?
}
