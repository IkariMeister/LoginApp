package com.ikarimeister.loginapp.utils

import com.ikarimeister.loginapp.domain.model.*

object MotherObject {
    const val ANY_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
    val email = Email("john.doe@company.com")
    val emptyEmail = Email("")
    val emptyPassword = Password("")
    val otherToken = Token("gdfgdfgdfgkfñdlgk")
    val password = Password("123456")
    val token = Token(ANY_TOKEN)
    val user = User(email, password)
    val profile = Profile(email, token)
}