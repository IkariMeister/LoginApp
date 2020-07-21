package com.ikarimeister.loginapp.data.network.dto

import com.ikarimeister.loginapp.domain.model.Email
import com.ikarimeister.loginapp.domain.model.Password
import com.ikarimeister.loginapp.domain.model.User

data class LoginRequest(val username: Email, val password: Password)

fun User.toDto() =
        LoginRequest(username = this.email, password = this.password)