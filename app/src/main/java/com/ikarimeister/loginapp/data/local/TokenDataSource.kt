package com.ikarimeister.loginapp.data.local

import arrow.core.Either
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.Token

interface TokenDataSource {
    fun get(): Either<StorageError, Token>
    operator fun plus(element: Token): Either<StorageError, Unit>
    operator fun minus(element: Token): Either<StorageError, Unit>
}