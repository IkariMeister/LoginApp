package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import arrow.core.extensions.fx
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.data.TokenRepository
import com.ikarimeister.loginapp.data.network.FakeLoginApiClient
import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.model.User

class Login(
    private val apiClient: LoginApiClient = Companion.apiClient,
    private val repository: TokenRepository = Companion.repository
) {
    operator fun invoke(user: User): Either<LoginError, Token> = Either.fx {
        val token = apiClient.login(user).bind()
        repository + token
        token
    }

    companion object {
        val apiClient by lazy { FakeLoginApiClient }
        val repository by lazy { TokenRepository() }
    }
}