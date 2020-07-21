package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.domain.model.*
import io.mockk.every
import io.mockk.mockkClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoginTest {

    private lateinit var stubApiClient: LoginApiClient
    private lateinit var sut: Login

    @Before
    fun setup() {
        stubApiClient = mockkClass(LoginApiClient::class)
        sut = Login(stubApiClient)
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
        val user = User(email = Email(""), password = Password(""))

        val actual = sut.invoke(user)

        assertEquals(token.right(), actual)
    }
}