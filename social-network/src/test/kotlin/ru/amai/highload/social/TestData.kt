package ru.amai.highload.social

import ru.amai.highload.social.domain.City
import ru.amai.highload.social.domain.Gender
import ru.amai.highload.social.domain.User
import ru.amai.highload.social.domain.UserPersonalDetails
import java.time.LocalDate
import java.time.Month

object UserPersonalDetailsData {

    val Test = UserPersonalDetails(
        id = 1,
        userId = 1,
        firstName = "Test",
        lastName = "Testov",
        birthDate = LocalDate.of(1992, Month.OCTOBER, 5),
        city = City(
            id = 1,
            name = "City17"
        ),
        gender = Gender(
            id = 1,
            name = "Male"
        )
    )

    val QA = UserPersonalDetails(
        id = 2,
        userId = 2,
        firstName = "QA",
        lastName = "QAov",
        birthDate = LocalDate.of(1972, 5, 5),
        city = City(
            id = 2,
            name = "Ghost City"
        ),
        gender = Gender(
            id = 2,
            name = "Female"
        )
    )
}

object UserData {

    val login1 = User(
        id = 1,
        login = "login1",
        password = "password123",
        roles = "USER"
    )

    val login2 = User(
        id = 2,
        login = "login2",
        password = "password123",
        roles = "USER"
    )
}
