package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import arrow.core.extensions.fx
import com.ikarimeister.loginapp.data.ConfigurationRepository
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.model.User

class Login(
    private val apiClient: LoginApiClient,
    private val repository: ConfigurationRepository<Token>
) {
    suspend operator fun invoke(user: User): Either<LoginError, Token> = Either.fx {
        val token = apiClient.login(user).bind()
        repository + token
        token
    }
}