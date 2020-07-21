package com.ikarimeister.loginapp.domain.model

data class User(val email: Email, val password: Password)

inline class Email(val value: String)

inline class Password(val value: String)

inline class Token(val value: String)