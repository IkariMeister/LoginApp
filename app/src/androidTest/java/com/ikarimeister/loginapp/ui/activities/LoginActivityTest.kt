package com.ikarimeister.loginapp.ui.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import arrow.core.left
import com.ikarimeister.loginapp.R
import com.ikarimeister.loginapp.asApp
import com.ikarimeister.loginapp.domain.model.IncorrectCredentials
import com.ikarimeister.loginapp.domain.model.NoConection
import com.ikarimeister.loginapp.domain.model.TokenNotFound
import com.ikarimeister.loginapp.domain.usecases.IsLoginStored
import com.ikarimeister.loginapp.domain.usecases.Login
import com.ikarimeister.loginapp.scopesModule
import com.ikarimeister.loginapp.ui.ActivityTest
import com.ikarimeister.loginapp.ui.presenter.LoginPresenter
import com.ikarimeister.loginapp.ui.view.LoginView
import com.ikarimeister.loginapp.utils.di.resetDI
import com.ikarimeister.loginapp.utils.matcher.IsTextInputLayoutShowingErrorMatcher
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions
import com.schibsted.spain.barista.interaction.BaristaClickInteractions
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test
import org.koin.core.qualifier.named
import org.koin.dsl.module

class LoginActivityTest : ActivityTest<LoginActivity>(LoginActivity::class.java) {

    @MockK
    lateinit var loginMock: Login

    @MockK
    lateinit var isLoginStored: IsLoginStored

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        val loginScopeMock = module {
            scope(named<LoginActivity>()) {
                factory { (view: LoginView) ->
                    LoginPresenter(view, loginMock, isLoginStored, Dispatchers.Unconfined, Dispatchers.Unconfined)
                }
            }
        }
        val app = InstrumentationRegistry.getInstrumentation().targetContext.asApp()
        app.resetDI(listOf(scopesModule), listOf(loginScopeMock))
    }

    @Test
    fun showLoginFormWhenTokenIsNotStored() {
        coEvery { isLoginStored() } returns TokenNotFound.left()

        startActivity()

        BaristaVisibilityAssertions.assertDisplayed(R.id.login)
        BaristaVisibilityAssertions.assertDisplayed(R.id.password)
        BaristaVisibilityAssertions.assertDisplayed(R.id.username)
        BaristaVisibilityAssertions.assertDisplayed(R.id.imageView)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.error)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.loading)
    }

    @Test
    fun showValidationErrorsWhenTokenIsNotStoredAndInputDataIsInvalid() {
        coEvery { isLoginStored() } returns TokenNotFound.left()

        startActivity()
        BaristaEditTextInteractions.writeTo(R.id.username, invalidEmail)
        BaristaEditTextInteractions.writeTo(R.id.password, invalidPassword)
        BaristaClickInteractions.clickOn(R.id.login)

        BaristaVisibilityAssertions.assertDisplayed(R.id.login)
        BaristaVisibilityAssertions.assertDisplayed(R.id.password)
        BaristaVisibilityAssertions.assertDisplayed(R.id.username)
        BaristaVisibilityAssertions.assertDisplayed(R.id.imageView)
        onView(withId(R.id.username)).check(matches(IsTextInputLayoutShowingErrorMatcher()))
        onView(withId(R.id.password)).check(matches(IsTextInputLayoutShowingErrorMatcher()))
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.loading)
    }

    @Test
    fun showValidationErrorsOnPaswordWhenTokenIsNotStoredAndInputPaswordIsInvalid() {
        coEvery { isLoginStored() } returns TokenNotFound.left()

        startActivity()
        BaristaEditTextInteractions.writeTo(R.id.username, validEmail)
        BaristaEditTextInteractions.writeTo(R.id.password, invalidPassword)
        BaristaClickInteractions.clickOn(R.id.login)

        BaristaVisibilityAssertions.assertDisplayed(R.id.login)
        BaristaVisibilityAssertions.assertDisplayed(R.id.password)
        BaristaVisibilityAssertions.assertDisplayed(R.id.username)
        BaristaVisibilityAssertions.assertDisplayed(R.id.imageView)
        onView(withId(R.id.username)).check(matches(IsTextInputLayoutShowingErrorMatcher(false)))
        onView(withId(R.id.password)).check(matches(IsTextInputLayoutShowingErrorMatcher()))
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.loading)
    }

    @Test
    fun showValidationErrorsOnEmailWhenTokenIsNotStoredAndInputEmailIsInvalid() {
        coEvery { isLoginStored() } returns TokenNotFound.left()

        startActivity()
        BaristaEditTextInteractions.writeTo(R.id.username, invalidEmail)
        BaristaEditTextInteractions.writeTo(R.id.password, validPassword)
        BaristaClickInteractions.clickOn(R.id.login)

        BaristaVisibilityAssertions.assertDisplayed(R.id.login)
        BaristaVisibilityAssertions.assertDisplayed(R.id.password)
        BaristaVisibilityAssertions.assertDisplayed(R.id.username)
        BaristaVisibilityAssertions.assertDisplayed(R.id.imageView)
        onView(withId(R.id.username)).check(matches(IsTextInputLayoutShowingErrorMatcher()))
        onView(withId(R.id.password)).check(matches(IsTextInputLayoutShowingErrorMatcher(false)))
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.loading)
    }

    @Test
    fun showIncorrectCredentialsMessageWhenApiReturnANotValidLoginResponse() {
        coEvery { isLoginStored() } returns TokenNotFound.left()
        coEvery { loginMock(any()) } returns IncorrectCredentials.left()

        val context = startActivity()
        BaristaEditTextInteractions.writeTo(R.id.username, validEmail)
        BaristaEditTextInteractions.writeTo(R.id.password, validPassword)
        BaristaClickInteractions.clickOn(R.id.login)

        BaristaVisibilityAssertions.assertDisplayed(R.id.login)
        BaristaVisibilityAssertions.assertDisplayed(R.id.password)
        BaristaVisibilityAssertions.assertDisplayed(R.id.username)
        BaristaVisibilityAssertions.assertDisplayed(R.id.imageView)
        BaristaVisibilityAssertions.assertDisplayed(R.id.error)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.loading)
        BaristaVisibilityAssertions.assertDisplayed(context.getString(R.string.login_failed))
    }

    @Test
    fun showSnackBarWhenThereIsNoConnection() {
        coEvery { isLoginStored() } returns TokenNotFound.left()
        coEvery { loginMock(any()) } returns NoConection.left()

        val context = startActivity()
        BaristaEditTextInteractions.writeTo(R.id.username, validEmail)
        BaristaEditTextInteractions.writeTo(R.id.password, validPassword)
        BaristaClickInteractions.clickOn(R.id.login)

        BaristaVisibilityAssertions.assertDisplayed(R.id.login)
        BaristaVisibilityAssertions.assertDisplayed(R.id.password)
        BaristaVisibilityAssertions.assertDisplayed(R.id.username)
        BaristaVisibilityAssertions.assertDisplayed(R.id.imageView)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.error)
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.loading)
        BaristaVisibilityAssertions.assertDisplayed(context.getString(R.string.no_connection))
    }

    companion object {
        const val validEmail = "john.doe@company.com"
        const val invalidEmail = "Not an email"
        const val validPassword = "123456"
        val invalidPassword = "A really too long password, really really long"
    }
}