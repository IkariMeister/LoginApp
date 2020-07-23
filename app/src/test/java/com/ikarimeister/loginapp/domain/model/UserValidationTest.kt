package com.ikarimeister.loginapp.domain.model

import arrow.core.extensions.validated.bifunctor.mapLeft
import org.junit.Assert
import org.junit.Test

class UserValidationTest {

    @Test
    fun `User should be invalid when email and password are empty`() {
        val email = Email("")
        val password = Password("")
        val expected = setOf(NotAnEmail, NotValidCharsInEmail, TooShortPassword)

        val actual = User.validate(email, password)

        Assert.assertTrue(actual.isInvalid)
        actual.mapLeft { Assert.assertEquals(expected, it.toList().toSet()) }
    }

    @Test
    fun `User should be invalid when email is valid but password is not`() {
        val email = Email("john.doe@company.com")
        val password = Password("12345678901234567")
        val expected = setOf(TooLongPassword)

        val actual = User.validate(email, password)

        Assert.assertTrue(actual.isInvalid)
        actual.mapLeft { Assert.assertEquals(expected, it.toList().toSet()) }
    }

    @Test
    fun `User should be invalid when password is valid but email is not`() {
        val email = Email("john.doecompany.com")
        val password = Password("123456")
        val expected = setOf(NotValidCharsInEmail, NotAnEmail)

        val actual = User.validate(email, password)

        Assert.assertTrue(actual.isInvalid)
        actual.mapLeft { Assert.assertEquals(expected, it.toList().toSet()) }
    }

    @Test
    fun `User should be valid when password and email are valid`() {
        val email = Email("john.doe@company.com")
        val password = Password("123456")
        val expected = User(email, password)

        val actual = User.validate(email, password)

        Assert.assertTrue(actual.isValid)
        actual.map { Assert.assertEquals(expected, it) }
    }

    @Test
    fun `Pasword is invalid when its length is less than 4`() {
        val email = Email("john.doe@company.com")
        val password = Password("123")
        val expected = setOf(TooShortPassword)

        val actual = User.validate(email, password)

        Assert.assertTrue(actual.isInvalid)
        actual.mapLeft { Assert.assertEquals(expected, it.toList().toSet()) }
    }

    @Test
    fun `Pasword is invalid when its length is greater than 16`() {
        val email = Email("john.doe@company.com")
        val password = Password("12345678901234567")
        val expected = setOf(TooLongPassword)

        val actual = User.validate(email, password)

        Assert.assertTrue(actual.isInvalid)
        actual.mapLeft { Assert.assertEquals(expected, it.toList().toSet()) }
    }

    @Test
    fun `Email is invalid when @ is not in the email`() {
        val email = Email("john.doecompany.com")
        val password = Password("123456")
        val expected = setOf(NotValidCharsInEmail, NotAnEmail)

        val actual = User.validate(email, password)

        Assert.assertTrue(actual.isInvalid)
        actual.mapLeft { Assert.assertEquals(expected, it.toList().toSet()) }
    }

    @Test
    fun `Email is invalid when emojis are in the email`() {
        val email = Email("john.doe😂@company.com")
        val password = Password("123456")
        val expected = setOf(NotValidCharsInEmail)

        val actual = User.validate(email, password)

        Assert.assertTrue(actual.isInvalid)
        actual.mapLeft { Assert.assertEquals(expected, it.toList().toSet()) }
    }

    @Test
    fun `Email is invalid when has more than 256 characters`() {
        val veryLongString = "ForthepresentationlayerMVPpatternisthechosenimplementationbecauseisthemost" +
                "familiarimplementationforthedevelopmentteamwecouldconsidermovingto@MVVMwithdatabindingbut" +
                "ourexpertiseandconfidencewithMVPmakeusfeelmorecomfortableForthethreadingproblemkotlinxdfdsfdfdsdf.Cor"
        val email = Email(veryLongString)
        val password = Password("123456")
        val expected = setOf(TooLongEmail)

        val actual = User.validate(email, password)

        Assert.assertTrue(actual.isInvalid)
        actual.mapLeft { Assert.assertEquals(expected, it.toList().toSet()) }
    }
}