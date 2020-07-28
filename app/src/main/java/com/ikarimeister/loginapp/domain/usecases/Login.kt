package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import arrow.core.extensions.fx
import com.ikarimeister.loginapp.data.ConfigurationRepository
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.Profile
import com.ikarimeister.loginapp.domain.model.User

class Login(
    private val apiClient: LoginApiClient,
    private val repository: ConfigurationRepository<Profile>
) {
    suspend operator fun invoke(user: User): Either<LoginError, Profile> = Either.fx {
        val token = apiClient.login(user).bind()
        val profile = Profile(user.email, token)
        repository + profile
        profile
    }
}