package ru.amai.highload.social.service

import ru.amai.highload.social.domain.Gender

interface GenderService {

    fun listAllGenders(): List<Gender>
    fun getGenderByName(name: String): Gender?
}
