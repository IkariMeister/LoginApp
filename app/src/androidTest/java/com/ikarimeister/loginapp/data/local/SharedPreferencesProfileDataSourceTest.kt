package com.ikarimeister.loginapp.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import arrow.core.Either
import com.google.gson.Gson
import com.ikarimeister.loginapp.asApp
import com.ikarimeister.loginapp.data.local.sharedpreferences.SharedPreferencesProfileDataSource
import com.ikarimeister.loginapp.domain.model.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named

private typealias DS = SharedPreferencesProfileDataSource

@LargeTest
@RunWith(AndroidJUnit4::class)
class SharedPreferencesProfileDataSourceTest {
    private lateinit var dataSource: ConfigurationDataSource<Profile>
    private lateinit var preferences: SharedPreferences
    private lateinit var gson: Gson

    companion object {
        const val ANY_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
        const val ANY_USERNAME = "john.doe@company.com"
        val anyProfile = Profile(Email(ANY_USERNAME), Token(ANY_TOKEN))
    }

    @Before
    fun setUp() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.asApp()

        preferences = app.getKoin().get()
        dataSource = app.getKoin().get(named("Profile"))
        gson = app.getKoin().get()
    }

    @After
    fun tearDown() {
        preferences.edit { clear() }
    }

    @Test
    fun itemShouldBeOnSharedpreferencesWhenIAddItOnDatasource() {
        val profile = anyProfile

        val result = dataSource + profile

        Assert.assertTrue(result is Either.Right<Unit>)
        Assert.assertTrue(preferences.contains(DS.ID))
        val actual = gson.fromJson(preferences.getString(DS.ID, "")!!, Profile::class.java)
        Assert.assertEquals(anyProfile, actual)
    }

    @Test
    fun itemShouldBeOnSharedpreferencesWhenIAddItOnDatasourceEvenIfDatasourceWasNotEmpty() {
        dataSource + Profile(Email(""), Token(""))

        val result = dataSource + anyProfile

        Assert.assertTrue(result is Either.Right<Unit>)
        Assert.assertTrue(preferences.contains(DS.ID))
        val actual = gson.fromJson(preferences.getString(DS.ID, "")!!, Profile::class.java)
        Assert.assertEquals(anyProfile, actual)
    }

    @Test
    fun itemIsReturnedByGetWhenIAddItOnDatasource() {
        dataSource + anyProfile

        val actual = dataSource.get()

        Assert.assertTrue(preferences.contains(DS.ID))
        Assert.assertTrue(actual is Either.Right<Profile>)
        actual.map { Assert.assertEquals(anyProfile, it) }
    }

    @Test
    fun theTokenNotFoundErrorIsReturnedByGetWhenDatasourceIsEmpty() {
        val actual = dataSource.get()

        Assert.assertFalse(preferences.contains(DS.ID))
        Assert.assertTrue(actual is Either.Left<StorageError>)
        actual.mapLeft { Assert.assertEquals(DataNotFound, it) }
    }

    @Test
    fun sharedPreferencesShouldBeEmptyWhenItemIsRemovedFromDataSource() {
        dataSource + anyProfile

        val result = dataSource - anyProfile

        Assert.assertTrue(result is Either.Right<Unit>)
        Assert.assertFalse(preferences.contains(DS.ID))
        val actual = dataSource.get()
        Assert.assertTrue(actual is Either.Left<StorageError>)
        actual.mapLeft { Assert.assertEquals(DataNotFound, it) }
    }
}