package ru.amai.highload.social.service.impl

import org.springframework.stereotype.Service
import ru.amai.highload.social.domain.UserPersonalDetails
import ru.amai.highload.social.repository.UserPersonalDetailsRepository
import ru.amai.highload.social.service.UserPersonalDetailsService
import ru.amai.highload.social.service.UserService

@Service
class UserPersonalDetailsServiceImpl(
    private val userPersonalDetailsRepository: UserPersonalDetailsRepository,
    private val userService: UserService
) : UserPersonalDetailsService {

    override fun getUserPersonalDetailsForUser(login: String): UserPersonalDetails? =
        userService.withUser(login) {
            userPersonalDetailsRepository.findByUserId(id!!)
        }

    override fun setUserPersonalDetails(userPersonalDetails: UserPersonalDetails): UserPersonalDetails =
        userPersonalDetailsRepository.save(userPersonalDetails)
}
