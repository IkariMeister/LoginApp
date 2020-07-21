package com.ikarimeister.loginapp.data.network.dto

import com.ikarimeister.loginapp.domain.model.Token

data class LoginResponse(val token: String)

fun LoginResponse.toDomain() = Token(this.token)