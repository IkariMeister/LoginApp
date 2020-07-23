package com.ikarimeister.loginapp.domain.usecases

import arrow.core.ValidatedNel
import arrow.core.invalidNel
import arrow.core.validNel

fun <E, T : E, V> V.validate(error: T, f: () -> Boolean): ValidatedNel<E, V> =
        validNel()
                .takeIf { f.invoke() }
                ?: error.invalidNel()