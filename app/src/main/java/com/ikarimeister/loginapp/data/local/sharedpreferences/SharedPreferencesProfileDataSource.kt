package com.ikarimeister.loginapp.data.local.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.data.local.ConfigurationDataSource
import com.ikarimeister.loginapp.data.local.JsonSerializer
import com.ikarimeister.loginapp.domain.model.DataNotFound
import com.ikarimeister.loginapp.domain.model.Profile
import com.ikarimeister.loginapp.domain.model.StorageError

class SharedPreferencesProfileDataSource(
    private val preferences: SharedPreferences,
    jsonSerializer: JsonSerializer<Profile>
) : ConfigurationDataSource<Profile>, JsonSerializer<Profile> by jsonSerializer {
    override fun get(): Either<StorageError, Profile> =
            if (preferences.contains(ID)) {
                preferences.getString(ID, null)
                        ?.right()?.map { it.fromJson(Profile::class.java) }
                        ?: DataNotFound.left()
            } else {
                DataNotFound.left()
            }

    override fun plus(element: Profile): Either<StorageError, Unit> =
            preferences.edit { putString(ID, element.toJson()) }.right()

    override fun minus(element: Profile): Either<StorageError, Unit> =
            preferences.edit { remove(ID) }.right()

    companion object {
        const val ID = "profile"
    }
}