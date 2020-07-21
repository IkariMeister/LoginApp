package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.data.network.FakeLoginApiClient
import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.model.User

class Login(private val apiClient: LoginApiClient = Dependencies.apiClient) {
    operator fun invoke(user: User): Either<LoginError, Token> =
            apiClient.login(user)

    companion object Dependencies {
        val apiClient = FakeLoginApiClient
    }
}