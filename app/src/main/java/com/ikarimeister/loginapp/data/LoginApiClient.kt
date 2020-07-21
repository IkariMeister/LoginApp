package com.ikarimeister.loginapp.data

import arrow.core.Either
import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.model.User

interface LoginApiClient {

    fun login(user: User): Either<LoginError, Token>
}