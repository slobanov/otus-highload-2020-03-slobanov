package ru.amai.highload.social.repository.impl

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import ru.amai.highload.social.domain.City
import ru.amai.highload.social.repository.CityRepository

@DataJdbcTest
@Transactional
@Import(CityJdbcRepository::class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class CityJdbcRepositoryTest {

    @Autowired
    private lateinit var cityRepository: CityRepository

    @Test
    fun findAllTest() {
        assertThat(cityRepository.findAll())
            .hasSize(3)
            .extracting<String>(City::name)
            .contains("City17", "Ghost City", "TeamCity")
    }

    @Test
    fun findByNameExistsTest() {
        val name = "City17"
        val city = cityRepository.findByName(name)

        assertThat(city)
            .`as`("City with name '$name'")
            .isNotNull
            .extracting { it!!.name }
            .`as`("Name of the $city")
            .isEqualTo(name)

        assertThat(city!!.id)
            .`as`("id of the $city with name '$name'")
            .isNotNull()
    }

    @Test
    fun findByNameNotExistsTest() {
        val name = "City77"
        assertThat(cityRepository.findByName(name))
            .`as`("City with name '$name'")
            .isNull()
    }

    @Test
    fun saveTest() {
        val oldCityCnt = cityRepository.findAll().size

        val newCityName = "newCity"
        val newCity = City(name = newCityName)

        val savedNewCity = cityRepository.save(newCity)

        assertThat(savedNewCity.name)
            .`as`("name of the saved city")
            .isEqualTo(newCityName)

        assertThat(savedNewCity.id)
            .`as`("id of the saved city")
            .isNotNull()

        assertThat(cityRepository.findAll().size)
            .`as`("new size of the repository")
            .isEqualTo(oldCityCnt + 1)
    }

    @Test
    fun saveDuplicateTest() {
        val newCity = City(name = "City17")
        assertThatExceptionOfType(DuplicateKeyException::class.java)
            .isThrownBy { cityRepository.save(newCity) }
    }

    @Test
    fun saveAndFindTest() {
        val newCityName = "newCity"
        val newCity = City(name = newCityName)

        assertThat(cityRepository.save(newCity))
            .`as`("new saved city with name '$newCityName'")
            .isEqualTo(cityRepository.findByName(newCityName))
    }
}
