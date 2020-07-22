package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.data.TokenRepository
import com.ikarimeister.loginapp.domain.model.*
import io.mockk.Called
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoginTest {

    private lateinit var stubApiClient: LoginApiClient
    private lateinit var mockRepository: TokenRepository
    private lateinit var sut: Login

    @Before
    fun setup() {
        stubApiClient = mockkClass(LoginApiClient::class)
        mockRepository = mockkClass(TokenRepository::class)
        sut = Login(stubApiClient, mockRepository)
    }

    @Test
    fun `Should return a LoginError when LoginApiClient returns any login error`() {
        every { stubApiClient.login(any()) } returns NoConection.left()
        val user = User(email = Email(""), password = Password(""))

        val actual = sut.invoke(user)

        assertTrue(actual is Either.Left<LoginError>)
    }

    @Test
    fun `Should return a Token when LoginApiClient returns a Token`() {
        val token = Token("")
        every { stubApiClient.login(any()) } returns token.right()
        every { mockRepository.plus(token) } returns Unit.right()
        val user = User(email = Email(""), password = Password(""))

        val actual = sut.invoke(user)

        assertEquals(token.right(), actual)
    }

    @Test
    fun `Should save nothing on the repository when LoginApiClient returns any login error`() {
        every { stubApiClient.login(any()) } returns NoConection.left()
        val user = User(email = Email(""), password = Password(""))

        sut.invoke(user)

        verify { mockRepository wasNot Called }
    }

    @Test
    fun `Should save on repository a Token when LoginApiClient returns a Token`() {
        val token = Token("")
        every { stubApiClient.login(any()) } returns token.right()
        every { mockRepository.plus(token) } returns Unit.right()
        val user = User(email = Email(""), password = Password(""))

        sut.invoke(user)

        verify { mockRepository + token }
    }
}