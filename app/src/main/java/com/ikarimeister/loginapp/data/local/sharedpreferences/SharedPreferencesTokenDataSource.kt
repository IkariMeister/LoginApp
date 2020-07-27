package com.ikarimeister.loginapp.data.local.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.data.local.ConfigurationDataSource
import com.ikarimeister.loginapp.domain.model.DataNotFound
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.Token

class SharedPreferencesTokenDataSource(
    private val preferences: SharedPreferences
) : ConfigurationDataSource<Token> {
    override fun get(): Either<StorageError, Token> =
            if (preferences.contains(ID)) {
                preferences.getString(ID, null)
                        ?.right()?.map { Token(it) }
                        ?: DataNotFound.left()
            } else {
                DataNotFound.left()
            }

    override fun plus(element: Token): Either<StorageError, Unit> =
            preferences.edit { putString(ID, element.value) }.right()

    override fun minus(element: Token): Either<StorageError, Unit> =
            preferences.edit { remove(ID) }.right()

    companion object {
        const val ID = "token"
    }
}