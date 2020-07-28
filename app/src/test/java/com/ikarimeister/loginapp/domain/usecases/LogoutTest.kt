package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.data.ConfigurationRepository
import com.ikarimeister.loginapp.domain.model.DataNotFound
import com.ikarimeister.loginapp.domain.model.Profile
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.UnknownStorageError
import com.ikarimeister.loginapp.utils.MotherObject
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LogoutTest {

    @MockK
    lateinit var repository: ConfigurationRepository<Profile>

    lateinit var logout: Logout

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        logout = Logout(repository)
    }

    @Test
    fun `should remove the token from repository if present and return Right Unit`() = runBlockingTest {
        every { repository.get() } returns MotherObject.profile.right()
        every { repository - MotherObject.profile } returns Unit.right()

        val actual = logout()

        Assert.assertTrue(actual is Either.Right<Unit>)
    }

    @Test
    fun `should return DataNotFound if repository is empty`() = runBlockingTest {
        every { repository.get() } returns DataNotFound.left()

        val actual = logout()

        Assert.assertTrue(actual is Either.Left<StorageError>)
        actual.mapLeft { Assert.assertEquals(DataNotFound, it) }
    }

    @Test
    fun `should return UnknownStorageError if there is any problem removing the token`() = runBlockingTest {
        every { repository.get() } returns MotherObject.profile.right()
        val throwable = Exception()
        every { repository - MotherObject.profile } returns UnknownStorageError(throwable).left()

        val actual = logout()

        Assert.assertTrue(actual is Either.Left<StorageError>)
        actual.mapLeft { Assert.assertTrue(it is UnknownStorageError) }
    }
}