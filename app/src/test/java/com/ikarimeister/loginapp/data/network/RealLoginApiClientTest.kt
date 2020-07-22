package com.ikarimeister.loginapp.data.network

import arrow.core.Either
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RealLoginApiClientTest : MockWebServerTest() {
    private lateinit var apiClient: LoginApiClient

    companion object {
        val anyUser = User(email = Email("jcgseco@gmail.com"), password = Password("123456"))
        const val ANY_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
    }

    @Before
    override fun setup() {
        super.setup()
        val mockWebServerEndpoint = baseEndpoint
        apiClient = RealLoginApiClient(mockWebServerEndpoint)
    }

    @Test
    fun `sends Accept and ContentType headers`() {
        enqueueMockResponse(403)

        apiClient.login(anyUser)

        assertRequestContainsHeader("Accept", "application/json")
    }

    @Test
    fun `sends login to correct endpoint and correct Http method`() {
        enqueueMockResponse(403)

        apiClient.login(anyUser)

        assertPostRequestSentTo("/login/")
    }

    @Test
    fun `sends login correct body`() {
        enqueueMockResponse(200, "login/response.json")

        apiClient.login(anyUser)

        assertRequestBodyEquals("login/request.json")
    }

    @Test
    fun `Token Retrieved when success login response is received`() {
        enqueueMockResponse(200, "login/response.json")
        val expected = Token(ANY_TOKEN)

        val actual = apiClient.login(anyUser)

        assert(actual is Either.Right<Token>)
        actual.map { assertEquals(expected, it) }
    }

    @Test
    fun `InvalidCredentials error when failure login response is received`() {
        enqueueMockResponse(403)

        val actual = apiClient.login(anyUser)

        assert(actual is Either.Left<LoginError>)
        actual.mapLeft { assertEquals(IncorrectCredentials, it) }
    }

    @Test
    fun `NoConnection error when any other response is received`() {
        enqueueMockResponse(500)

        val actual = apiClient.login(anyUser)

        assert(actual is Either.Left<LoginError>)
        actual.mapLeft { assertEquals(NoConection, it) }
    }
}