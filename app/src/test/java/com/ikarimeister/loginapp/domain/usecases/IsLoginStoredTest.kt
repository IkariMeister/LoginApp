package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.data.repositories.TokenRepository
import com.ikarimeister.loginapp.domain.model.DataNotFound
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.utils.MotherObject.token
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class IsLoginStoredTest {

    @MockK
    lateinit var repository: TokenRepository
    private lateinit var sut: IsLoginStored

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = IsLoginStored(repository)
    }

    @Test
    fun `should return a TokenNotFound error when repository is empty`() = runBlockingTest {
        every { repository.get() } returns DataNotFound.left()

        val actual = sut()

        Assert.assertTrue(actual is Either.Left<StorageError>)
        actual.mapLeft { Assert.assertEquals(DataNotFound, it) }
    }

    @Test
    fun `should return a Token when repository has the token`() = runBlockingTest {
        every { repository.get() } returns token.right()

        val actual = sut()

        Assert.assertTrue(actual is Either.Right<Token>)
        actual.map { Assert.assertEquals(token, it) }
    }
}