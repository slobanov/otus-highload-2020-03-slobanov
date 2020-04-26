package ru.amai.highload.social.service.impl

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.amai.highload.social.domain.Gender
import ru.amai.highload.social.repository.GenderRepository

@ExtendWith(MockKExtension::class)
internal class GenderServiceImplTest {

    @MockK
    private lateinit var genderRepository: GenderRepository

    @InjectMockKs
    private lateinit var genderService: GenderServiceImpl

    @Test
    fun listAllGendersTest() {
        val genders = listOf(
            Gender(name = "1"),
            Gender(name = "2")
        )
        every { genderRepository.findAll() } returns genders
        assertThat(genderService.listAllGenders())
            .isEqualTo(genders)
        verify(exactly = 1) { genderRepository.findAll() }
    }
}
