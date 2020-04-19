package ru.amai.highload.social.repository.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import ru.amai.highload.social.domain.Interest
import ru.amai.highload.social.repository.InterestRepository

@DataJdbcTest
@Transactional
@Import(InterestJdbcRepository::class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class InterestJdbcRepositoryTest {

    @Autowired
    private lateinit var interestRepository: InterestRepository

    @Test
    fun findByUserIdExistsTest() {
        val userId = 1L
        assertThat(interestRepository.findByUserId(userId))
            .`as`("Interests for user with id '$userId'")
            .extracting<String>(Interest::name)
            .contains("Witchcraft", "Sleeping")
    }

    @Test
    fun findByUserIdNotExistsTest() {
        val userId = 42L
        assertThat(interestRepository.findByUserId(userId))
            .`as`("Interests for non-existent user (assuming, he has no interests whatsoever)")
            .isEmpty()
    }

    @Test
    fun findAllTest() {
        assertThat(interestRepository.findAll())
            .hasSize(3)
            .`as`("All interests in repository")
            .extracting<String>(Interest::name)
            .contains("Witchcraft", "Sleeping", "Green")
    }

    @Test
    fun mergeAllNewInterestsTest() {
        val newInterests = listOf(
            Interest(name = "1"),
            Interest(name = "2")
        )

        val oldAllInterests = interestRepository.findAll()
        val oldSize = oldAllInterests.size

        assertThat(oldAllInterests).matches(
            { old -> old.none { interest ->
                newInterests.any { it.name == interest.name }
            } },
            "does not contains any interests from $newInterests"
        )

        val savedNewInterests = interestRepository.mergeAll(newInterests)
        val newAllInterests = interestRepository.findAll()

        assertThat(newAllInterests)
            .`as`("repository size after new interests addition")
            .hasSize(oldSize + newInterests.size)

        assertThat(newAllInterests)
            .containsAll(savedNewInterests)

        assertThat(savedNewInterests)
            .`as`("new saved interest's names")
            .extracting<String>(Interest::name)
            .containsAll(newInterests.map(Interest::name))

        assertThat(savedNewInterests)
            .`as`("new saved interest's ids")
            .extracting<Long>(Interest::id)
            .doesNotContain(null)
    }

    @Test
    fun mergeAllTest() {
        val newInterests = listOf(
            Interest(name = "Green"),
            Interest(name = "3")
        )

        val oldAllInterests = interestRepository.findAll()
        val oldSize = oldAllInterests.size

        val savedNewInterests = interestRepository.mergeAll(newInterests)
        val newAllInterests = interestRepository.findAll()

        assertThat(newAllInterests)
            .`as`("repository size after new interests addition (only 1 interest must've been added)")
            .hasSize(oldSize + 1)

        checkNewSavedInterests(
            savedNewInterests = savedNewInterests,
            newInterests = newInterests,
            newAllInterests = newAllInterests
        )
    }

    private fun checkNewSavedInterests(
        savedNewInterests: List<Interest>,
        newInterests: List<Interest>,
        newAllInterests: List<Interest>
    ) {
        assertThat(newAllInterests)
            .containsAll(savedNewInterests)

        assertThat(savedNewInterests)
            .extracting<String>(Interest::name)
            .containsAll(newInterests.map(Interest::name))

        assertThat(savedNewInterests)
            .`as`("new saved interest's names")
            .extracting<String>(Interest::name)
            .containsAll(newInterests.map(Interest::name))

        assertThat(savedNewInterests)
            .`as`("new saved interest's ids")
            .extracting<Long>(Interest::id)
            .doesNotContain(null)
    }

    @Test
    fun setUserIdTest() {
        val userId = 2L

        val witchCraftInterest = interestRepository
            .findAll()
            .filter { it.name == "Witchcraft" }

        assertThat(interestRepository.findByUserId(userId))
            .`as`("Interests of user with id '$userId' before `setUserId`")
            .doesNotContain(*witchCraftInterest.toTypedArray())

        interestRepository.setUserId(userId, witchCraftInterest)

        assertThat(interestRepository.findByUserId(userId))
            .`as`("Interests of user with id '$userId' after `setUserId`")
            .contains(*witchCraftInterest.toTypedArray())
    }
}
