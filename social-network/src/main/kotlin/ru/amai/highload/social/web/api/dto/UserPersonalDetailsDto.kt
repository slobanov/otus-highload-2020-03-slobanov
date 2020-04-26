package ru.amai.highload.social.web.api.dto

import ru.amai.highload.social.domain.UserPersonalDetails
import ru.amai.highload.social.domain.age

class UserPersonalDetailsDto(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val city: String,
    val gender: String
)

fun UserPersonalDetails.toDto(): UserPersonalDetailsDto =
    UserPersonalDetailsDto(
        firstName = firstName,
        lastName = lastName,
        age = age,
        gender = gender.name,
        city = city.name
    )
