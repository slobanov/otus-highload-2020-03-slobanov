package ru.amai.highload.social.web.api

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.amai.highload.social.domain.Following
import ru.amai.highload.social.service.FollowerService
import ru.amai.highload.social.web.API
import ru.amai.highload.social.web.FOLLOWED
import ru.amai.highload.social.web.LOGIN_PARAM
import ru.amai.highload.social.web.USER
import java.security.Principal

@RestController
@RequestMapping("/$API/$FOLLOWED")
class FollowedResource(
    private val followerService: FollowerService
) {

    @GetMapping("/$USER/{$LOGIN_PARAM}")
    fun getFollowedByUser(@PathVariable login: String): ResponseEntity<List<String>> {
        val followedLoginList = followerService.listFollowedBy(login)
        return if (followedLoginList.isNotEmpty()) {
            ok(followedLoginList)
        } else { noContent().build() }
    }

    @GetMapping("/{$LOGIN_PARAM}")
    fun canFollow(principal: Principal, @PathVariable login: String): ResponseEntity<Boolean> =
        ok(followerService.canFollow(
            currentLogin = principal.name,
            userToFollowLogin = login)
        )

    @PostMapping("/{$LOGIN_PARAM}")
    fun follow(principal: Principal, @PathVariable login: String): ResponseEntity<Following> =
        ok(followerService.follow(
            currentLogin = principal.name,
            userToFollowLogin = login
        ))

    @ExceptionHandler(Exception::class)
    fun userAlreadyExists(): ResponseEntity<Nothing> =
        status(BAD_REQUEST).build()
}
