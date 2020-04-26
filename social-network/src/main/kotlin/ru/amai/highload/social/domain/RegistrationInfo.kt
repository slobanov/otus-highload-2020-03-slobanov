package ru.amai.highload.social.domain

import java.time.LocalDate

data class RegistrationInfo(
    val login: String,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val city: String,
    val gender: String,
    val interests: List<String>,
    val password: String
)
