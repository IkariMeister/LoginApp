package com.ikarimeister.loginapp.data

import arrow.core.Either
import com.ikarimeister.loginapp.data.local.SharedPreferencesTokenDataSource
import com.ikarimeister.loginapp.data.local.TokenDataSource
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.Token

class TokenRepository(private val datasource: TokenDataSource = Companion.datasource) {

    fun get(): Either<StorageError, Token> = datasource.get()
    operator fun plus(element: Token): Either<StorageError, Unit> = datasource + element
    operator fun minus(element: Token): Either<StorageError, Unit> = datasource - element

    companion object {
        val datasource by lazy { SharedPreferencesTokenDataSource() }
    }
}