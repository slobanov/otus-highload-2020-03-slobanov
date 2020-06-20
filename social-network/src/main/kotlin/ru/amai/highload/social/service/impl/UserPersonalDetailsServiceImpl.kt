package ru.amai.highload.social.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.amai.highload.social.domain.User
import ru.amai.highload.social.domain.UserPersonalDetails
import ru.amai.highload.social.domain.UserPersonalDetailsLight
import ru.amai.highload.social.service.UserPersonalDetailsSearchSpeck
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

    override fun searchUserPersonalDetailsLight(
        limit: Int,
        offset: Long,
        searchSpeck: UserPersonalDetailsSearchSpeck
    ): List<UserPersonalDetailsLight> {
        val boundedLimit = min(limit, maxLimit)
        val userPersonalDetailsList = searchSpeck(boundedLimit, offset)

        val userIds = userPersonalDetailsList.mapNotNull(UserPersonalDetails::id)
        return if (userIds.isNotEmpty()) {
            val userById = userService.listUsersByIds(userIds).associateBy(User::id)
            userPersonalDetailsList.map { userPersonalDetails ->
                UserPersonalDetailsLight(
                    login = userById.getValue(userPersonalDetails.userId).login,
                    firstName = userPersonalDetails.firstName,
                    lastName = userPersonalDetails.lastName
                )
            }
        } else emptyList()
    }

    private operator fun UserPersonalDetailsSearchSpeck.invoke(limit: Int, offset: Long) =
        with(this) { when {
            firstNamePrefix.isNotBlank() && lastNamePrefix.isNotBlank() ->
                userPersonalDetailsRepository.findByFirstNamePrefixAndLastNamePrefixOrderById(
                    firstNamePrefix = firstNamePrefix,
                    lastNamePrefix = lastNamePrefix,
                    limit = limit,
                    offset = offset
                )
            else -> userPersonalDetailsRepository.findAllOrderById(
                limit = limit,
                offset = offset
            )
        } }

    override fun setUserPersonalDetails(userPersonalDetails: UserPersonalDetails): UserPersonalDetails =
        userPersonalDetailsRepository.save(userPersonalDetails)

    private companion object {
        const val DEFAULT_MAX_LIMIT = 100
    }
}
