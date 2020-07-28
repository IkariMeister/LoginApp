package com.ikarimeister.loginapp.domain.usecases

import com.ikarimeister.loginapp.data.ConfigurationRepository
import com.ikarimeister.loginapp.domain.model.Profile

class GetProfile(private val repository: ConfigurationRepository<Profile>) {
    suspend operator fun invoke() = repository.get()
}