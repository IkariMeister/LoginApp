package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import arrow.core.extensions.either.monad.flatten
import com.ikarimeister.loginapp.data.TokenRepository
import com.ikarimeister.loginapp.domain.model.StorageError

class Logout(private val repository: TokenRepository = Companion.repository) {

    suspend operator fun invoke(): Either<StorageError, Unit> = repository.get().map { repository - it }.flatten()

    companion object {
        private val repository by lazy { TokenRepository() }
    }
}