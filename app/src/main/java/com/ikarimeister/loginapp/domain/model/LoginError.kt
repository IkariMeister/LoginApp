package com.ikarimeister.loginapp.domain.model

sealed class LoginError

object NoConection : LoginError()
object IncorrectCredentials : LoginError()