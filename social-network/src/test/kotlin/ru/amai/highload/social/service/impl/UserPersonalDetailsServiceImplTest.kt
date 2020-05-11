package ru.amai.highload.social.service.impl

import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forNone
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ru.amai.highload.social.UserData.login1
import ru.amai.highload.social.UserData.login2
import ru.amai.highload.social.UserPersonalDetailsData.QA
import ru.amai.highload.social.UserPersonalDetailsData.Test
import ru.amai.highload.social.domain.User
import ru.amai.highload.social.domain.UserPersonalDetails
import ru.amai.highload.social.repository.UserPersonalDetailsRepository
import ru.amai.highload.social.service.UserPersonalDetailsSearchSpeck
import ru.amai.highload.social.service.UserPersonalDetailsService
import ru.amai.highload.social.service.UserService

class UserPersonalDetailsServiceImplTest : StringSpec({
    val userPersonalDetailsRepository =
        mockk<UserPersonalDetailsRepository>("userPersonalDetailsRepository")
    val userService = mockk<UserService>("userService")
    val maxLimit = 5000
    val userPersonalDetailsService: UserPersonalDetailsService = UserPersonalDetailsServiceImpl(
        userPersonalDetailsRepository,
        userService,
        maxLimit
    )

    "personal details for real user exists" {
        val user = mockk<User>("user")
        val login = "login1"
        val id = 42L
        val userPersonalDetails = mockk<UserPersonalDetails>("userPersonalDetails")

        every { user.id } returns id
        every { userService.withUser<UserPersonalDetails>(login, any()) } answers {
            secondArg<User.() -> UserPersonalDetails>().invoke(user)
        }
        every { userPersonalDetailsRepository.findByUserId(id) } returns userPersonalDetails

        userPersonalDetailsService.getUserPersonalDetailsForUser(login) shouldBe userPersonalDetails

        verify(exactly = 1) { userService.withUser<UserPersonalDetails>(login, any()) }
        verify(exactly = 1) { userPersonalDetailsRepository.findByUserId(id) }
    }

    "personal details for fake user don't exist" {
        every { userService.withUser<UserPersonalDetails>(any(), any()) } returns null

        userPersonalDetailsService.getUserPersonalDetailsForUser("any") shouldBe null

        verify(exactly = 1) { userService.withUser<UserPersonalDetails>(any(), any()) }
        verify(exactly = 0) { userPersonalDetailsRepository.findByUserId(any()) }
    }

    "set user personal details" {
        val userPersonalDetails = mockk<UserPersonalDetails>("userPersonalDetails")
        val savedUserPersonalDetails = mockk<UserPersonalDetails>("savedUserPersonalDetails")

        every { userPersonalDetailsRepository.save(userPersonalDetails) } returns savedUserPersonalDetails

        userPersonalDetailsService.setUserPersonalDetails(userPersonalDetails) shouldBe savedUserPersonalDetails

        verify(exactly = 1) { userPersonalDetailsRepository.save(userPersonalDetails) }
    }

    "find all users, no firstNamePrefix" {
        val allUsers = listOf(QA, Test)
        val searchSpeck = mockk<UserPersonalDetailsSearchSpeck>("searchSpeck")
        val limit = 10
        val offset = 42L

        every { searchSpeck.firstNamePrefix } returns ""
        every { userPersonalDetailsRepository.findAllOrderById(any(), any()) } returns allUsers
        every { userService.listUsersByIds(any()) } returns listOf(login1, login2)

        userPersonalDetailsService.searchUserPersonalDetailsLight(
            limit = limit,
            offset = offset,
            searchSpeck = searchSpeck
        ).forOne { user ->
            user.firstName shouldBe "QA"
        }.forOne { user ->
            user.firstName shouldBe "Test"
        }.shouldHaveSize(2)

        verify(exactly = 1) { userPersonalDetailsRepository.findAllOrderById(any(), any()) }
        verify(exactly = 1) { userService.listUsersByIds(any()) }
    }

    "search users by name" {
        val allUsers = listOf(QA)
        val searchSpeck = mockk<UserPersonalDetailsSearchSpeck>("searchSpeck")
        val limit = 10
        val offset = 42L
        val firstNamePrefix = "123"
        val lastNamePrefix = "456"

        every { searchSpeck.firstNamePrefix } returns firstNamePrefix
        every { searchSpeck.lastNamePrefix } returns lastNamePrefix
        every { userPersonalDetailsRepository.findByFirstNamePrefixAndLastNamePrefixOrderById(
            firstNamePrefix,
            lastNamePrefix,
            any(),
            any()
        ) } returns allUsers
        every { userService.listUsersByIds(any()) } returns listOf(login2)

        userPersonalDetailsService.searchUserPersonalDetailsLight(
            limit = limit,
            offset = offset,
            searchSpeck = searchSpeck
        ).forOne { user ->
            user.firstName shouldBe "QA"
        }.forNone { user ->
            user.firstName shouldBe "Test"
        }.shouldHaveSize(1)
    }

    afterTest {
        clearMocks(userPersonalDetailsRepository, userService)
    }
})
