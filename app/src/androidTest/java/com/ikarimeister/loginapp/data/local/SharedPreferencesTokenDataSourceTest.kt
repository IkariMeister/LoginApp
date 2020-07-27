package com.ikarimeister.loginapp.data.local

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import arrow.core.Either
import com.ikarimeister.loginapp.asApp
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.model.DataNotFound
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

typealias ActualDS = SharedPreferencesTokenDataSource

@LargeTest
@RunWith(AndroidJUnit4::class)
class SharedPreferencesTokenDataSourceTest {
    private lateinit var dataSource: TokenDataSource
    private lateinit var preferences: SharedPreferences

    companion object {
        const val ANY_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
    }

    @Before
    fun setUp() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.asApp()
        preferences = app.getSharedPreferences(ActualDS.ID, MODE_PRIVATE)
        dataSource = ActualDS(preferences)
    }

    @After
    fun tearDown() {
        preferences.edit { clear() }
    }

    @Test
    fun itemShouldBeOnSharedpreferencesWhenIAddItOnDatasource() {
        val token = Token(ANY_TOKEN)

        val result = dataSource + token

        Assert.assertTrue(result is Either.Right<Unit>)
        Assert.assertTrue(preferences.contains(ActualDS.ID))
        val actual = preferences.getString(ActualDS.ID, "")!!
        Assert.assertEquals(ANY_TOKEN, actual)
    }

    @Test
    fun itemShouldBeOnSharedpreferencesWhenIAddItOnDatasourceEvenIfDatasourceWasNotEmpty() {
        dataSource + Token("")
        val token = Token(ANY_TOKEN)

        val result = dataSource + token

        Assert.assertTrue(result is Either.Right<Unit>)
        Assert.assertTrue(preferences.contains(ActualDS.ID))
        val actual = preferences.getString(ActualDS.ID, "")!!
        Assert.assertEquals(ANY_TOKEN, actual)
    }

    @Test
    fun itemIsReturnedByGetWhenIAddItOnDatasource() {
        val token = Token(ANY_TOKEN)
        dataSource + token

        val actual = dataSource.get()

        Assert.assertTrue(preferences.contains(ActualDS.ID))
        Assert.assertTrue(actual is Either.Right<Token>)
        actual.map { Assert.assertEquals(token, it) }
    }

    @Test
    fun theTokenNotFoundErrorIsReturnedByGetWhenDatasourceIsEmpty() {
        val actual = dataSource.get()

        Assert.assertFalse(preferences.contains(ActualDS.ID))
        Assert.assertTrue(actual is Either.Left<StorageError>)
        actual.mapLeft { Assert.assertEquals(DataNotFound, it) }
    }

    @Test
    fun sharedPreferencesShouldBeEmptyWhenItemIsRemovedFromDataSource() {
        val token = Token(ANY_TOKEN)
        dataSource + token

        val result = dataSource - token

        Assert.assertTrue(result is Either.Right<Unit>)
        Assert.assertFalse(preferences.contains(ActualDS.ID))
        val actual = dataSource.get()
        Assert.assertTrue(actual is Either.Left<StorageError>)
        actual.mapLeft { Assert.assertEquals(DataNotFound, it) }
    }
}