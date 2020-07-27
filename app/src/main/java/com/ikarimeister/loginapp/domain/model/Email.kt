package com.ikarimeister.loginapp.domain.model

import arrow.core.NonEmptyList
import arrow.core.Validated
import arrow.core.extensions.nonemptylist.semigroup.semigroup
import arrow.core.extensions.validated.applicative.applicative
import arrow.core.fix
import com.ikarimeister.loginapp.domain.usecases.validate

inline class Email(val value: String) {
    companion object {
        const val EMAIL_REGEX = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@" +
                "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
                "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." +
                "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
                "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|" +
                "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
        const val maxLength = 256
    }

    fun validate() =
            Validated.applicative(NonEmptyList.semigroup<ValidationErrors>()).mapN(
                    this.validate(NotAnEmail) { this.value.contains("@") },
                    this.validate(TooLongEmail) { this.value.length < maxLength },
                    this.validate(NotValidCharsInEmail) { this.value.matches(EMAIL_REGEX.toRegex()) }
            ) { it.a }.fix()
}