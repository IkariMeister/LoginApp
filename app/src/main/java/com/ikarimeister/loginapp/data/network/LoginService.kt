package com.ikarimeister.loginapp.data.network

import com.ikarimeister.loginapp.data.network.dto.LoginRequest
import com.ikarimeister.loginapp.data.network.dto.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login/")
    fun login(@Body login: LoginRequest): Call<LoginResponse>
}