package ru.amai.highload.social.repository

import ru.amai.highload.social.domain.City

interface CityRepository {

    fun findAll(): List<City>
    fun findByName(name: String): City?
    fun save(city: City): City
}
