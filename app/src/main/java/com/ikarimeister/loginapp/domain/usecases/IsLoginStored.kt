package com.ikarimeister.loginapp.domain.usecases

import com.ikarimeister.loginapp.data.TokenRepository

class IsLoginStored(private val repository: TokenRepository) {
    suspend operator fun invoke() = repository.get()
}