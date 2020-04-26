package ru.amai.highload.social.web.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.amai.highload.social.domain.RegistrationInfo
import ru.amai.highload.social.service.RegistrationService
import ru.amai.highload.social.service.impl.UserAlreadyExistsException
import ru.amai.highload.social.web.API
import ru.amai.highload.social.web.SIGN_UP
import ru.amai.highload.social.web.api.dto.UserDto
import ru.amai.highload.social.web.api.dto.toDto

@RestController
@RequestMapping("/$API/$SIGN_UP")
class RegistrationResource(
    private val registrationService: RegistrationService
) {

    @PostMapping
    fun register(@RequestBody registrationInfo: RegistrationInfo): ResponseEntity<UserDto> =
        ok(registrationService.register(registrationInfo).toDto())

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun userAlreadyExists(): ResponseEntity<Nothing> =
        status(HttpStatus.CONFLICT).build()
}
