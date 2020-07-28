package com.ikarimeister.loginapp.data.repositories

import arrow.core.Either
import com.ikarimeister.loginapp.data.ConfigurationRepository
import com.ikarimeister.loginapp.data.local.ConfigurationDataSource
import com.ikarimeister.loginapp.domain.model.Profile
import com.ikarimeister.loginapp.domain.model.StorageError

class ProfileRepository(private val datasource: ConfigurationDataSource<Profile>) : ConfigurationRepository<Profile> {

    override fun get(): Either<StorageError, Profile> = datasource.get()
    override operator fun plus(element: Profile): Either<StorageError, Unit> = datasource + element
    override operator fun minus(element: Profile): Either<StorageError, Unit> = datasource - element
}