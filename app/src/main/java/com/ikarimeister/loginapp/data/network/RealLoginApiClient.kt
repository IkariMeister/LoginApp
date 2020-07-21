package com.ikarimeister.loginapp.data.network

import arrow.core.Either
import arrow.core.left
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.data.network.dto.toDomain
import com.ikarimeister.loginapp.data.network.dto.toDto
import com.ikarimeister.loginapp.data.network.interceptor.DefaultHeadersInterceptor
import com.ikarimeister.loginapp.domain.model.IncorrectCredentials
import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.NoConection
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.model.User
import java.io.IOException
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RealLoginApiClient(val baseEndpoint: String) : LoginApiClient {

    private val service by lazy {
        val client = OkHttpClient().newBuilder().addInterceptor(DefaultHeadersInterceptor()).build()
        val retrofit = Retrofit.Builder()
                .baseUrl(baseEndpoint)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        retrofit.create(LoginService::class.java)
    }

    override fun login(user: User): Either<LoginError, Token> = try {
        val response = service.login(user.toDto()).execute()
        inspectResponseForErrors(response).map { it.toDomain() }
    } catch (e: IOException) {
        NoConection.left()
    }

    @Suppress("MagicNumber")
    private fun <T> inspectResponseForErrors(response: Response<T>): Either<LoginError, T> = when {
        response.code() == 403 -> IncorrectCredentials.left()
        response.code() >= 400 -> NoConection.left()
        response.body() == null -> IncorrectCredentials.left()
        else -> Either.right((response.body()!!))
    }
}