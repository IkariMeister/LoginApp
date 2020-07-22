package com.ikarimeister.loginapp.data

import arrow.core.Either
import com.ikarimeister.loginapp.data.local.TokenDataSource
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.Token

class TokenRepository(private val datasource: TokenDataSource) {

    fun get(): Either<StorageError, Token> = datasource.get()
    operator fun plus(element: Token): Either<StorageError, Unit> = datasource + element
    operator fun minus(element: Token): Either<StorageError, Unit> = datasource - element
}