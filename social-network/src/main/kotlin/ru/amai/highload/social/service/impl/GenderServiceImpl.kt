package ru.amai.highload.social.service.impl

import org.springframework.stereotype.Service
import ru.amai.highload.social.domain.Gender
import ru.amai.highload.social.repository.GenderRepository
import ru.amai.highload.social.service.GenderService

@Service
class GenderServiceImpl(
    private val genderRepository: GenderRepository
) : GenderService {

    override fun listAllGenders(): List<Gender> = genderRepository.findAll()
    override fun getGenderByName(name: String): Gender? = genderRepository.findByName(name)
}
