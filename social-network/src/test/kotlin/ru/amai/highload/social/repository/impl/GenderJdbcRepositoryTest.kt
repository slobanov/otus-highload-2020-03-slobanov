package ru.amai.highload.social.repository.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import ru.amai.highload.social.domain.Gender
import ru.amai.highload.social.repository.GenderRepository

@DataJdbcTest
@Transactional
@Import(GenderJdbcRepository::class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class GenderJdbcRepositoryTest {

    @Autowired
    private lateinit var genderRepository: GenderRepository

    @Test
    fun findAllTest() {
        assertThat(genderRepository.findAll())
            .hasSize(2)
            .extracting<String>(Gender::name)
            .contains("Male", "Female")
    }

    @Test
    fun findByNameExistsTest() {
        val genderName = "Male"
        val gender = genderRepository.findByName(genderName)

        assertThat(gender)
            .`as`("Gender with name '$genderName'")
            .isNotNull
            .extracting { it!!.name }
            .`as`("Name of the $gender")
            .isEqualTo(genderName)

        assertThat(gender!!.id)
            .`as`("id of the $gender with name '$genderName'")
            .isNotNull()
    }

    @Test
    fun findByNameNotExistsTest() {
        val genderName = "Orange"
        assertThat(genderRepository.findByName(genderName))
            .`as`("Gender with name '$genderName'")
            .isNull()
    }
}
