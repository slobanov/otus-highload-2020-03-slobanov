package ru.amai.highload.social.web.api

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.amai.highload.social.domain.City
import ru.amai.highload.social.service.CityService
import ru.amai.highload.social.web.API
import ru.amai.highload.social.web.CITY

@RestController
@RequestMapping("/$API/$CITY")
class CityResource(
    private val cityService: CityService
) {

    @GetMapping("/")
    fun getAllCities(): ResponseEntity<List<String>> {
        val cities = cityService.listAllCities()
        return if (cities.isNotEmpty()) {
            ok(cities.map(City::name))
        } else {
            noContent().build()
        }
    }
}
