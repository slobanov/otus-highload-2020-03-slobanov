package ru.amai.highload.social.web.api

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.amai.highload.social.domain.Gender
import ru.amai.highload.social.service.GenderService
import ru.amai.highload.social.web.API
import ru.amai.highload.social.web.GENDER

@RestController
@RequestMapping("/$API/$GENDER")
class GenderResource(
    private val genderService: GenderService
) {

    @GetMapping
    fun getAllGenders(): ResponseEntity<List<String>> {
        val genders = genderService.listAllGenders()
        return if (genders.isNotEmpty()) {
            ok(genders.map(Gender::name))
        } else {
            noContent().build()
        }
    }
}
