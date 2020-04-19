package ru.amai.highload.social.web.security

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.amai.highload.social.service.UserService

@Service
class SocialUserDetailsService(
    private val userService: UserService
) : UserDetailsService {

    override fun loadUserByUsername(login: String): UserDetails =
        userService.withUser(login) {
            User.builder()
                .username(login)
                .password(password)
                .roles(*roles.split(",").toTypedArray())
                .build()
        } ?: throw UsernameNotFoundException("Failed to find user")
}
