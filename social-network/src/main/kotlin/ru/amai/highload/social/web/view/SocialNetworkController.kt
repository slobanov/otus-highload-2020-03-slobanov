package ru.amai.highload.social.web.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView
import ru.amai.highload.social.web.LOGIN_PARAM
import ru.amai.highload.social.web.SIGN_IN
import ru.amai.highload.social.web.SIGN_UP
import ru.amai.highload.social.web.USER
import ru.amai.highload.social.web.USERS
import java.security.Principal

@Controller
class SocialNetworkController {

    @GetMapping("/$SIGN_IN")
    @Suppress("FunctionOnlyReturningConstant")
    fun login(): String = SIGN_IN

    @GetMapping("/$SIGN_UP")
    @Suppress("FunctionOnlyReturningConstant")
    fun signUp(): String = SIGN_UP

    @GetMapping("/")
    fun homepage(principal: Principal): ModelAndView =
        ModelAndView("redirect:/$USER/${principal.name}")

    @GetMapping("/$USER/{$LOGIN_PARAM}")
    @Suppress("FunctionOnlyReturningConstant")
    fun user(@PathVariable login: String): String = USER

    @GetMapping("/$USERS")
    fun users(): String = USERS
}
