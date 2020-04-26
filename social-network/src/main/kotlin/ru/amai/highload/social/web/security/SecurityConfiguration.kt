package ru.amai.highload.social.web.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.amai.highload.social.web.API
import ru.amai.highload.social.web.CITY
import ru.amai.highload.social.web.GENDER
import ru.amai.highload.social.web.INTEREST
import ru.amai.highload.social.web.LOGIN
import ru.amai.highload.social.web.SIGN_IN
import ru.amai.highload.social.web.SIGN_UP

@Configuration
class SecurityConfiguration(
    private val userDetailsService: UserDetailsService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/$SIGN_IN").permitAll()
            .antMatchers("/$SIGN_UP").permitAll()
            .antMatchers("/$API/$SIGN_UP").permitAll()
            .antMatchers("/$API/$CITY/").permitAll()
            .antMatchers("/$API/$GENDER/").permitAll()
            .antMatchers("/$API/$INTEREST/").permitAll()
            .antMatchers("/css/*").permitAll()
            .antMatchers("/js/*").permitAll()
            .antMatchers("/favicon.ico").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/$SIGN_IN")
            .loginProcessingUrl("/$LOGIN")
            .defaultSuccessUrl("/")
            .and()
            .rememberMe()
            .rememberMeCookieName("social-network-token")
    }

    override fun configure(builder: AuthenticationManagerBuilder) {
        builder.userDetailsService(userDetailsService)
    }
}

@Configuration
class PasswordEncoderConfiguration {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
