package ru.amai.highload.social.domain

import java.time.LocalDate
import java.time.LocalDate.now
import java.time.temporal.ChronoUnit.YEARS

data class UserPersonalDetails(
    val id: Long? = null,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val gender: Gender,
    val city: City
)

val UserPersonalDetails.age: Int
    get() = YEARS.between(birthDate, now()).toInt()
