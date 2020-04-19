package ru.amai.highload.social.web.api

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.amai.highload.social.service.UserPersonalDetailsService
import ru.amai.highload.social.web.API
import ru.amai.highload.social.web.LOGIN_PARAM
import ru.amai.highload.social.web.USER
import ru.amai.highload.social.web.api.dto.UserPersonalDetailsDto
import ru.amai.highload.social.web.api.dto.toDto

@RestController
@RequestMapping("/$API/$USER")
class UserPersonalDetailsResource(
    private val userPersonalDetailsService: UserPersonalDetailsService
) {

    @GetMapping("{$LOGIN_PARAM}")
    fun getUserPersonalDetails(
        @PathVariable login: String
    ): ResponseEntity<UserPersonalDetailsDto> =
        userPersonalDetailsService.getUserPersonalDetailsForUser(login)?.let {
            userPersonalDetails -> ok(userPersonalDetails.toDto())
        } ?: noContent().build()
}
