package ru.amai.highload.social.service

import ru.amai.highload.social.domain.RegistrationInfo
import ru.amai.highload.social.domain.User

interface RegistrationService {

    fun register(registrationInfo: RegistrationInfo): User
}
