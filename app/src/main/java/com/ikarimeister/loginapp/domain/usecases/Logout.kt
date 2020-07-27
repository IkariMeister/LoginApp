package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import arrow.core.extensions.either.monad.flatten
import com.ikarimeister.loginapp.data.ConfigurationRepository
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.Token

class Logout(private val repository: ConfigurationRepository<Token>) {

    suspend operator fun invoke(): Either<StorageError, Unit> = repository.get().map { repository - it }.flatten()
}