package ru.amai.highload.social.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.amai.highload.social.domain.User
import ru.amai.highload.social.domain.UserPersonalDetails
import ru.amai.highload.social.domain.UserPersonalDetailsLight
import ru.amai.highload.social.repository.UserPersonalDetailsRepository
import ru.amai.highload.social.service.UserPersonalDetailsService
import ru.amai.highload.social.service.UserService
import java.lang.Integer.min

@Service
class UserPersonalDetailsServiceImpl(
    private val userPersonalDetailsRepository: UserPersonalDetailsRepository,
    private val userService: UserService,
    @Value("\${social.user-details.max-limit:$DEFAULT_MAX_LIMIT}") private val maxLimit: Int
) : UserPersonalDetailsService {

    override fun getUserPersonalDetailsForUser(login: String): UserPersonalDetails? =
        userService.withUser(login) {
            userPersonalDetailsRepository.findByUserId(id!!)
        }

    override fun getAllUserPersonalDetailsLight(limit: Int, offset: Long): List<UserPersonalDetailsLight> {
        val users = userService.listUsers(min(limit, maxLimit), offset)
        val loginByUserId = users.associateBy(User::id, User::login)
        return userPersonalDetailsRepository.findAllByUserIds(users.mapNotNull(User::id))
            .filter { it.userId in loginByUserId }
            .map { userPersonalDetails ->
                UserPersonalDetailsLight(
                    login = loginByUserId.getValue(userPersonalDetails.userId),
                    firstName = userPersonalDetails.firstName,
                    lastName = userPersonalDetails.lastName
                )
            }
    }

    override fun setUserPersonalDetails(userPersonalDetails: UserPersonalDetails): UserPersonalDetails =
        userPersonalDetailsRepository.save(userPersonalDetails)

    private companion object {
        const val DEFAULT_MAX_LIMIT = 100
    }
}
