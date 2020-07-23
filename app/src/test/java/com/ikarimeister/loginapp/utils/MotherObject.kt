package com.ikarimeister.loginapp.utils

import com.ikarimeister.loginapp.domain.model.Email
import com.ikarimeister.loginapp.domain.model.Password
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.model.User

object MotherObject {
    const val ANY_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
    val email = Email("john.doe@company.com")
    val password = Password("123456")
    val token = Token(ANY_TOKEN)
    val otherToken = Token("gdfgdfgdfgkfñdlgk")
    val user = User(email, password)
}