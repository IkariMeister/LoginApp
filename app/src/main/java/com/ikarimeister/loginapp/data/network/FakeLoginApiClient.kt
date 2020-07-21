package com.ikarimeister.loginapp.data.network

import arrow.core.Either
import arrow.core.right
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.data.network.dto.LoginResponse
import com.ikarimeister.loginapp.data.network.dto.toDomain
import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.model.User

object FakeLoginApiClient : LoginApiClient {
    private const val DEFAULT_TOKEN_VALUE = "ñdslkfñldkfñs"

    override fun login(user: User): Either<LoginError, Token> =
            LoginResponse(DEFAULT_TOKEN_VALUE).toDomain().right()
}