package com.ikarimeister.loginapp.data

import arrow.core.Either
import com.ikarimeister.loginapp.domain.model.StorageError

interface ConfigurationRepository<T> {
    fun get(): Either<StorageError, T>
    operator fun plus(element: T): Either<StorageError, Unit>
    operator fun minus(element: T): Either<StorageError, Unit>
}