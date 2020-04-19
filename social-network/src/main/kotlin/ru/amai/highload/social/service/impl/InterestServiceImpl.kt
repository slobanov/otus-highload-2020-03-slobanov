package ru.amai.highload.social.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.amai.highload.social.domain.Interest
import ru.amai.highload.social.repository.InterestRepository
import ru.amai.highload.social.service.InterestService
import ru.amai.highload.social.service.UserService

@Service
class InterestServiceImpl(
    private val interestRepository: InterestRepository,
    private val userService: UserService
) : InterestService {

    override fun interestsForUser(login: String): List<Interest> =
        userService.withUser(login) {
            interestRepository.findByUserId(id!!)
        } ?: emptyList()

    override fun allInterests(): List<Interest> = interestRepository.findAll()

    @Transactional
    override fun addInterestsForUser(userId: Long, interestNames: List<String>): List<Interest> {
        val interests = interestRepository.mergeAll(interestNames.map { Interest(name = it) })
        interestRepository.setUserId(userId, interests)
        return interests
    }
}
