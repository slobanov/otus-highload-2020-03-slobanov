package ru.amai.highload.social.service

import ru.amai.highload.social.domain.City

interface CityService {

    fun listAllCities(): List<City>
    fun ensureCity(name: String): City
}
