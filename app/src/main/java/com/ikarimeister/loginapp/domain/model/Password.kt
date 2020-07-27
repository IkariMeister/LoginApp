package com.ikarimeister.loginapp.domain.model

import arrow.core.NonEmptyList
import arrow.core.Validated
import arrow.core.extensions.nonemptylist.semigroup.semigroup
import arrow.core.extensions.validated.applicative.applicative
import arrow.core.fix
import com.ikarimeister.loginapp.domain.usecases.validate

inline class Password(val value: String) {
    companion object {
        const val minLength = 4
        const val maxLength = 16
    }

    fun validate() =
            Validated.applicative(NonEmptyList.semigroup<ValidationErrors>()).mapN(
                    this.validate(TooShortPassword) { this.value.length >= minLength },
                    this.validate(TooLongPassword) { this.value.length < maxLength }
            ) { it.a }.fix()
}