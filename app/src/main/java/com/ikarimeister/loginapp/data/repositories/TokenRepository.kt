package com.ikarimeister.loginapp.data.repositories

import arrow.core.Either
import com.ikarimeister.loginapp.data.ConfigurationRepository
import com.ikarimeister.loginapp.data.local.ConfigurationDataSource
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.Token

class TokenRepository(private val datasource: ConfigurationDataSource<Token>) : ConfigurationRepository<Token> {

    override fun get(): Either<StorageError, Token> = datasource.get()
    override operator fun plus(element: Token): Either<StorageError, Unit> = datasource + element
    override operator fun minus(element: Token): Either<StorageError, Unit> = datasource - element
}