package ru.amai.highload.social.domain

data class User(
    val id: Long? = null,
    val login: String,
    val password: String,
    val roles: String
)
