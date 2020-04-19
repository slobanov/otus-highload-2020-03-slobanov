package ru.amai.highload.social.repository.impl

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.springframework.dao.IncorrectResultSizeDataAccessException

internal class RepositoryUtilsKtTest {

    @Test
    fun asSingleEmptyListTest() {
        assertThat(listOf<String>().asSingle())
            .isNull()
    }

    @Test
    fun asSingleSingletonListTest() {
        val value = "1"
        assertThat(listOf(value).asSingle())
            .isEqualTo(value)
    }

    @Test
    fun asSingleListWith2ElementsTest() {
        assertThatExceptionOfType(IncorrectResultSizeDataAccessException::class.java)
            .isThrownBy { listOf(1, 2).asSingle() }
    }
}
