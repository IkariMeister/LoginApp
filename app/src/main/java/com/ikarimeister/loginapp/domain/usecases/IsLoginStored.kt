package com.ikarimeister.loginapp.domain.usecases

import com.ikarimeister.loginapp.data.TokenRepository

class IsLoginStored(private val repository: TokenRepository = Companion.repository) {
    suspend operator fun invoke() = repository.get()

    companion object {
        val repository: TokenRepository by lazy { TokenRepository() }
    }
}