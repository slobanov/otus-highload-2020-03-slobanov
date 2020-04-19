package ru.amai.highload.social.web.api

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.amai.highload.social.domain.Interest
import ru.amai.highload.social.service.InterestService
import ru.amai.highload.social.web.API
import ru.amai.highload.social.web.INTEREST
import ru.amai.highload.social.web.LOGIN_PARAM
import ru.amai.highload.social.web.USER

@RestController
@RequestMapping("/$API/$INTEREST/")
class InterestResource(
    private val interestService: InterestService
) {

    @GetMapping("/")
    fun getInterests(): ResponseEntity<List<String>> =
        interestService.allInterests().toResponseEntity()

    @GetMapping("$USER/{$LOGIN_PARAM}")
    fun getInterestsForUser(@PathVariable login: String): ResponseEntity<List<String>> =
        interestService.interestsForUser(login).toResponseEntity()

    private fun List<Interest>.toResponseEntity() =
        if (isNotEmpty()) ok(map(Interest::name))
        else noContent().build()
}
