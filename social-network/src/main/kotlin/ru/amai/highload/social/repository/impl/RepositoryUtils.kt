package ru.amai.highload.social.repository.impl

import org.springframework.dao.IncorrectResultSizeDataAccessException

fun <T> List<T>.asSingle(): T? = when (size) {
    0, 1 -> firstOrNull()
    else -> throw IncorrectResultSizeDataAccessException(1, size)
}
