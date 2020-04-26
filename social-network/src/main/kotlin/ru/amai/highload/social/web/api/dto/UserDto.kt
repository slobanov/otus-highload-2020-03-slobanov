package ru.amai.highload.social.web.api.dto

import ru.amai.highload.social.domain.User

data class UserDto(
    val login: String
)

fun User.toDto(): UserDto = UserDto(login = login)
