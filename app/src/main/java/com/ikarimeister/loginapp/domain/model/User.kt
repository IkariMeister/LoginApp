package com.ikarimeister.loginapp.domain.model

import arrow.core.NonEmptyList
import arrow.core.Validated
import arrow.core.extensions.nonemptylist.semigroup.semigroup
import arrow.core.extensions.validated.applicative.applicative
import arrow.core.fix
import com.ikarimeister.loginapp.domain.usecases.validate

data class User(val email: Email, val password: Password) {
    companion object {
        fun validate(email: Email, password: Password) =
                Validated.applicative(NonEmptyList.semigroup<ValidationErrors>()).mapN(
                        email.validate(), password.validate()
                ) { User(it.a, it.b) }.fix()
    }
}