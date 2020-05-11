package ru.amai.highload.social.repository.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import ru.amai.highload.social.UserPersonalDetailsData.QA
import ru.amai.highload.social.UserPersonalDetailsData.Test
import ru.amai.highload.social.domain.City
import ru.amai.highload.social.domain.Gender
import ru.amai.highload.social.domain.UserPersonalDetails
import ru.amai.highload.social.repository.UserPersonalDetailsRepository
import java.time.LocalDate

@DataJdbcTest
@Transactional
@Import(UserPersonalDetailsJdbcRepository::class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class UserPersonalDetailsJdbcRepositoryTest {

    @Autowired
    private lateinit var userPersonalDetailsRepository: UserPersonalDetailsRepository

    @Test
    fun findByUserIdExistsTest() {
        val userId = 1L
        val userPersonalDetails = userPersonalDetailsRepository.findByUserId(userId)

        with(userPersonalDetails) {
            assertThat(this).isNotNull()
            assertThat(this!!.id).`as`("id").isNotNull()
            assertThat(userId).`as`("userId").isEqualTo(userId)
            assertThat(firstName).`as`("firstName").isEqualTo("Test")
            assertThat(lastName).`as`("lastName").isEqualTo("Testov")
            assertThat(city.id).`as`("city id").isNotNull()
            assertThat(city.name).`as`("city name").isEqualTo("City17")
            assertThat(gender.id).`as`("gender id").isNotNull()
            assertThat(gender.name).`as`("gender name").isEqualTo("Male")
        }
    }

    @Test
    fun findByUserIdNotExistsTest() {
        val userId = 42L
        assertThat(userPersonalDetailsRepository.findByUserId(userId))
            .`as`("personal details for user with id '$userId'")
            .isNull()
    }

    @Test
    fun saveTest() {
        val userId = 3L
        val firstName = "1"
        val lastName = "2"
        val birthDate = LocalDate.of(2000, 10, 1)
        val gender = Gender(id = 1, name = "Male")
        val city = City(id = 1, name = "City17")

        val userPersonalDetails = UserPersonalDetails(
            userId = userId,
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate,
            gender = gender,
            city = city
        )

        val savedUserPersonalDetails = userPersonalDetailsRepository.save(userPersonalDetails)
        val retrievedUserPersonalDetails = userPersonalDetailsRepository.findByUserId(userId)

        assertThat(retrievedUserPersonalDetails).isNotNull()
        assertThat(savedUserPersonalDetails)
            .isEqualTo(retrievedUserPersonalDetails)

        fun checkPersonalDetails(personalDetails: UserPersonalDetails) {
            with(personalDetails) {
                assertThat(id).`as`("id").isNotNull()
                assertThat(userId).`as`("userId").isEqualTo(userId)
                assertThat(firstName).`as`("firstName").isEqualTo("1")
                assertThat(lastName).`as`("lastName").isEqualTo("2")
                assertThat(city).`as`("city").isEqualTo(city)
                assertThat(gender).`as`("gender").isEqualTo(gender)
                assertThat(birthDate).`as`("birth date").isEqualTo(LocalDate.of(2000, 10, 1))
            }
        }

        checkPersonalDetails(savedUserPersonalDetails)
        checkPersonalDetails(retrievedUserPersonalDetails!!)
    }

    @ParameterizedTest(name = "{displayName} [limit = {0} offset = {1}]")
    @DisplayName("all users ordered by id")
    @MethodSource(
        "ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepositoryTestKt#usersProvider"
    )
    fun findAllOrderById(
        limit: Int,
        offset: Long,
        expectedUsers: List<UserPersonalDetails>
    ) {
        val users = userPersonalDetailsRepository.findAllOrderById(
            limit = limit,
            offset = offset
        )
        assertThat(users).isEqualTo(expectedUsers)
    }

    @ParameterizedTest(name = """
        {displayName} [limit = {0} offset = {1}, 
firstName starts with {2}, lastName starts with {3}]
    """)
    @DisplayName("users ordered by id, filtered by firstName and lastName")
    @MethodSource(
        "ru.amai.highload.social.repository.impl.UserPersonalDetailsJdbcRepositoryTestKt#usersWithNameProvider"
    )
    fun findByFirstNamePrefixAndLastNamePrefixOrderById(
        limit: Int,
        offset: Long,
        firstNamePrefix: String,
        lastNamePrefix: String,
        expectedUsers: List<UserPersonalDetails>
    ) {
        val users = userPersonalDetailsRepository.findByFirstNamePrefixAndLastNamePrefixOrderById(
            limit = limit,
            offset = offset,
            firstNamePrefix = firstNamePrefix,
            lastNamePrefix = lastNamePrefix
        )
        assertThat(users).isEqualTo(expectedUsers)
    }
}

private fun usersProvider() = listOf(
    of(0, 1, emptyList<UserPersonalDetails>()),
    of(1, 1, listOf(Test)),
    of(2, 0, listOf(QA, Test))
)

private fun usersWithNameProvider() = listOf(
    of(0, 100, "T", "T", emptyList<UserPersonalDetails>()),
    of(10, 0, "T", "T", listOf(Test)),
    of(10, 0, "A", "T", emptyList<UserPersonalDetails>())
)
