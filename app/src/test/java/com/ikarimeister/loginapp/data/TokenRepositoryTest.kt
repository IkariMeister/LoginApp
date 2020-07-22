package com.ikarimeister.loginapp.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.data.local.TokenDataSource
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.model.TokenNotFound
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TokenRepositoryTest {

    companion object {
        const val ANY_TOKEN = "fdgfdgfdgfdhfghdf"
    }

    lateinit var repository: TokenRepository

    @MockK
    lateinit var dataSource: TokenDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = TokenRepository(dataSource)
    }

    @Test
    fun `Token is returned from datasource when datasource has a token`() {
        val token = Token(ANY_TOKEN)
        every { dataSource.get() } returns token.right()

        val actual = repository.get()

        assertTrue(actual is Either.Right<Token>)
        verify { dataSource.get() }
        actual.map { assertEquals(token, it) }
    }

    @Test
    fun `TokenNotFound is returned from datasource when datasource has no token`() {
        every { dataSource.get() } returns TokenNotFound.left()

        val actual = repository.get()

        assertTrue(actual is Either.Left<StorageError>)
        actual.mapLeft { assertEquals(TokenNotFound, it) }
    }
}