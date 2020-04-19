package ru.amai.highload.social.service.impl

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource
import ru.amai.highload.social.domain.City
import ru.amai.highload.social.repository.CityRepository

@ExtendWith(MockKExtension::class)
internal class CityServiceImplTest {

    @MockK
    lateinit var cityRepository: CityRepository

    @InjectMockKs
    lateinit var cityService: CityServiceImpl

    @ParameterizedTest
    @MethodSource("citiesProvider")
    fun listAllCitiesTest(cities: List<City>) {
        every { cityRepository.findAll() } returns cities
        assertThat(cityService.listAllCities())
            .isEqualTo(cities)
        verify(exactly = 1) { cityRepository.findAll() }
    }

    @Test
    fun ensureCityTestAlreadyExists() {
        val cityName = "abc"
        val city = City(name = "abc")
        every { cityRepository.findByName(cityName) } returns city
        assertThat(cityService.ensureCity(cityName))
            .`as`("Existing city with name '$cityName'")
            .isEqualTo(city)
        verify(exactly = 1) { cityRepository.findByName(cityName) }
        verify(inverse = true) { cityRepository.save(any()) }
    }

    @Test
    fun ensureCityTestNewCity() {
        val cityName = "abc"
        val city = City(name = "abc")
        every { cityRepository.findByName(any()) } returns null

        val citySlot = slot<City>()
        every { cityRepository.save(city = capture(citySlot)) } returns city
        assertThat(cityService.ensureCity(cityName))
            .`as`("New city with name '$cityName'")
            .isEqualTo(city)
        assertThat(citySlot.captured).isEqualTo(city)
        verify(exactly = 1) { cityRepository.findByName(cityName) }
        verify(exactly = 1) { cityRepository.save(city) }
    }

    companion object {
        @JvmStatic
        @Suppress("Unused")
        fun citiesProvider() = listOf(
            of(listOf<City>()),
            of(listOf(City(name = "123"))),
            of(listOf(
                City(name = "aaa"),
                City(name = "bbb")
            ))
        )
    }
}
