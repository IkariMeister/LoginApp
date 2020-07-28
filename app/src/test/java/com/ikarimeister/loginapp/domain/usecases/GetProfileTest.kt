package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.data.ConfigurationRepository
import com.ikarimeister.loginapp.domain.model.DataNotFound
import com.ikarimeister.loginapp.domain.model.Profile
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.utils.MotherObject
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetProfileTest {

    @MockK
    lateinit var repository: ConfigurationRepository<Profile>
    private lateinit var sut: GetProfile

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = GetProfile(repository)
    }

    @Test
    fun `should return a TokenNotFound error when repository is empty`() = runBlockingTest {
        coEvery { repository.get() } returns DataNotFound.left()

        val actual = sut()

        Assert.assertTrue(actual is Either.Left<StorageError>)
        actual.mapLeft { Assert.assertEquals(DataNotFound, it) }
    }

    @Test
    fun `should return a Token when repository has the token`() = runBlockingTest {
        coEvery { repository.get() } returns MotherObject.profile.right()

        val actual = sut()

        Assert.assertTrue(actual is Either.Right<Profile>)
        actual.map { Assert.assertEquals(MotherObject.profile, it) }
    }
}