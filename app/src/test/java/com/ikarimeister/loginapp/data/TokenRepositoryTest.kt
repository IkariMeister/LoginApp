package com.ikarimeister.loginapp.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.data.local.ConfigurationDataSource
import com.ikarimeister.loginapp.data.repositories.TokenRepository
import com.ikarimeister.loginapp.domain.model.DataNotFound
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.utils.MotherObject.token
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TokenRepositoryTest {

    private lateinit var repository: TokenRepository

    @MockK
    lateinit var dataSource: ConfigurationDataSource<Token>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = TokenRepository(dataSource)
    }

    @Test
    fun `Token is returned from datasource when datasource has a token`() {
        every { dataSource.get() } returns token.right()

        val actual = repository.get()

        assertTrue(actual is Either.Right<Token>)
        verify { dataSource.get() }
        actual.map { assertEquals(token, it) }
    }

    @Test
    fun `DataNotFound is returned from datasource when datasource has no token`() {
        every { dataSource.get() } returns DataNotFound.left()

        val actual = repository.get()

        assertTrue(actual is Either.Left<StorageError>)
        actual.mapLeft { assertEquals(DataNotFound, it) }
    }
}