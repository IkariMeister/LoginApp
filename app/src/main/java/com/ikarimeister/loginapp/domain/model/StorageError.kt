package com.ikarimeister.loginapp.domain.model

sealed class StorageError
object TokenNotFound : StorageError()
data class UnknownStorageError(val t: Throwable) : StorageError()