package com.ikarimeister.loginapp.domain.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.data.ConfigurationRepository
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.NoConection
import com.ikarimeister.loginapp.domain.model.Profile
import com.ikarimeister.loginapp.utils.MotherObject
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoginTest {

    @MockK
    lateinit var stubApiClient: LoginApiClient

    @MockK
    lateinit var mockRepository: ConfigurationRepository<Profile>
    private lateinit var sut: Login

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = Login(stubApiClient, mockRepository)
    }

    @Test
    fun `Should return a LoginError when LoginApiClient returns any login error`() = runBlocking {
        every { stubApiClient.login(any()) } returns NoConection.left()

        val actual = sut.invoke(MotherObject.user)

        assertTrue(actual is Either.Left<LoginError>)
    }

    @Test
    fun `Should return a Token when LoginApiClient returns a Token`() = runBlocking {
        every { stubApiClient.login(any()) } returns MotherObject.token.right()
        every { mockRepository + MotherObject.profile } returns Unit.right()

        val actual = sut.invoke(MotherObject.user)

        assertEquals(MotherObject.profile.right(), actual)
    }

    @Test
    fun `Should save nothing on the repository when LoginApiClient returns any login error`() = runBlocking {
        every { stubApiClient.login(any()) } returns NoConection.left()

        sut.invoke(MotherObject.user)

        verify { mockRepository wasNot Called }
    }

    @Test
    fun `Should save on repository a Token when LoginApiClient returns a Token`() = runBlocking {
        every { stubApiClient.login(any()) } returns MotherObject.token.right()
        every { mockRepository + MotherObject.profile } returns Unit.right()

        sut.invoke(MotherObject.user)

        verify { mockRepository + MotherObject.profile }
    }
}