package com.ikarimeister.loginapp.ui.activities

import androidx.test.platform.app.InstrumentationRegistry
import arrow.core.left
import com.ikarimeister.loginapp.R
import com.ikarimeister.loginapp.asApp
import com.ikarimeister.loginapp.domain.model.TokenNotFound
import com.ikarimeister.loginapp.domain.usecases.Logout
import com.ikarimeister.loginapp.scopesModule
import com.ikarimeister.loginapp.ui.ActivityTest
import com.ikarimeister.loginapp.ui.presenter.MainPresenter
import com.ikarimeister.loginapp.ui.view.MainView
import com.ikarimeister.loginapp.utils.di.resetDI
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions
import com.schibsted.spain.barista.interaction.BaristaClickInteractions
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test
import org.koin.core.qualifier.named
import org.koin.dsl.module

class MainActivityTest : ActivityTest<MainActivity>(MainActivity::class.java) {

    @MockK
    lateinit var logoutMock: Logout

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        val mainScopeMock = module {
            scope(named<MainActivity>()) {
                factory { (view: MainView) ->
                    MainPresenter(view, logoutMock, Dispatchers.Unconfined, Dispatchers.Unconfined)
                }
            }
        }
        val app = InstrumentationRegistry.getInstrumentation().targetContext.asApp()
        app.resetDI(listOf(scopesModule), listOf(mainScopeMock))
    }

    @Test
    fun showErrorWhenLogoutReturnsError() {
        coEvery { logoutMock() } returns TokenNotFound.left()

        val context = startActivity()
        BaristaClickInteractions.clickOn(R.id.logout)

        BaristaVisibilityAssertions.assertDisplayed(context.getString(R.string.unknown_error))
    }

    @Test
    fun activityShowsWelcomeMessageAndLogoutButton() {
        coEvery { logoutMock() } returns TokenNotFound.left()

        val context = startActivity()

        BaristaVisibilityAssertions.assertDisplayed(R.id.logout)
        BaristaVisibilityAssertions.assertDisplayed(context.getString(R.string.action_sign_out))
        BaristaVisibilityAssertions.assertDisplayed(R.id.message)
        BaristaVisibilityAssertions.assertDisplayed(context.getString(R.string.welcome))
    }
}