package com.ikarimeister.loginapp.domain.usecases

import com.ikarimeister.loginapp.data.ConfigurationRepository
import com.ikarimeister.loginapp.domain.model.Token

class IsLoginStored(private val repository: ConfigurationRepository<Token>) {
    suspend operator fun invoke() = repository.get()
}