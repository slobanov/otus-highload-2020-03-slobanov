package ru.amai.highload.social.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.amai.highload.social.domain.City
import ru.amai.highload.social.repository.CityRepository
import ru.amai.highload.social.service.CityService

@Service
class CityServiceImpl(
    private val cityRepository: CityRepository
) : CityService {

    override fun listAllCities(): List<City> = cityRepository.findAll()

    @Transactional
    override fun ensureCity(name: String): City = with(cityRepository) {
        findByName(name) ?: save(City(name = name))
    }
}
