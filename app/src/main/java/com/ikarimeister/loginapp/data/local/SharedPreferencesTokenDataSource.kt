package com.ikarimeister.loginapp.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.model.TokenNotFound

class SharedPreferencesTokenDataSource(
    private val preferences: SharedPreferences
) : TokenDataSource {
    override fun get(): Either<StorageError, Token> =
            if (preferences.contains(ID)) {
                preferences.getString(ID, null)
                        ?.right()?.map { Token(it) }
                        ?: TokenNotFound.left()
            } else {
                TokenNotFound.left()
            }

    override fun plus(element: Token): Either<StorageError, Unit> =
            preferences.edit { putString(ID, element.value) }.right()

    override fun minus(element: Token): Either<StorageError, Unit> =
            preferences.edit { remove(ID) }.right()

    companion object {
        const val ID = "token"
    }
}