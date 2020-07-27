package com.ikarimeister.loginapp.domain.model

sealed class StorageError
object DataNotFound : StorageError()
data class UnknownStorageError(val t: Throwable) : StorageError()