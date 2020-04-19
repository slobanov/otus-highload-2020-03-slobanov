package ru.amai.highload.social.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.amai.highload.social.domain.RegistrationInfo
import ru.amai.highload.social.domain.User
import ru.amai.highload.social.domain.UserPersonalDetails
import ru.amai.highload.social.service.CityService
import ru.amai.highload.social.service.GenderService
import ru.amai.highload.social.service.InterestService
import ru.amai.highload.social.service.RegistrationService
import ru.amai.highload.social.service.UserPersonalDetailsService
import ru.amai.highload.social.service.UserService

@Service
class RegistrationServiceImpl(
    private val userService: UserService,
    private val userPersonalDetailsService: UserPersonalDetailsService,
    private val genderService: GenderService,
    private val cityService: CityService,
    val interestService: InterestService
) : RegistrationService {

    @Transactional
    override fun register(registrationInfo: RegistrationInfo): User = with(registrationInfo) {
        val user = try {
            userService.createNewUser(
                login = login,
                password = password
            )
        } catch (iae: IllegalArgumentException) {
            throw UserAlreadyExistsException(login, iae)
        }
        requireNotNull(user.id) { "User id must not be null" }

        val genderObj = genderService.getGenderByName(gender)
        requireNotNull(genderObj) { "There is no gender with name '$gender'" }

        val cityObj = cityService.ensureCity(city)

        if (registrationInfo.interests.isNotEmpty()) {
            interestService.addInterestsForUser(user.id, registrationInfo.interests)
        }

        userPersonalDetailsService.setUserPersonalDetails(
            UserPersonalDetails(
                userId = user.id,
                firstName = firstName,
                lastName = lastName,
                birthDate = birthDate,
                gender = genderObj,
                city = cityObj
            )
        )

        return user
    }
}

class UserAlreadyExistsException(
    private val login: String,
    e: Exception
) : Exception(e)
