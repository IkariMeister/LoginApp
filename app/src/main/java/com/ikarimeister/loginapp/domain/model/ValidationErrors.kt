package com.ikarimeister.loginapp.domain.model

sealed class ValidationErrors

sealed class EmailValidationErrors : ValidationErrors()
sealed class PasswordValidationErrors : ValidationErrors()

object TooShortPassword : PasswordValidationErrors()
object TooLongPassword : PasswordValidationErrors()
object NotAnEmail : EmailValidationErrors()
object NotValidCharsInEmail : EmailValidationErrors()
object TooLongEmail : EmailValidationErrors()