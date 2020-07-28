package com.ikarimeister.loginapp.ui.activities

import androidx.test.platform.app.InstrumentationRegistry
import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.R
import com.ikarimeister.loginapp.asApp
import com.ikarimeister.loginapp.domain.model.DataNotFound
import com.ikarimeister.loginapp.domain.model.Email
import com.ikarimeister.loginapp.domain.model.Profile
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.usecases.GetProfile
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
import leakcanary.FailTestOnLeak
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

class MainActivityTest : ActivityTest<MainActivity>(MainActivity::class.java) {

    @MockK
    lateinit var logoutMock: Logout
    @MockK
    lateinit var getProfileMock: GetProfile
    private lateinit var mainScopeMock: Module

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mainScopeMock = module {
            scope(named<MainActivity>()) {
                factory { (view: MainView) ->
                    MainPresenter(view, getProfileMock, logoutMock, Dispatchers.Unconfined, Dispatchers.Unconfined)
                }
            }
        }
        val app = InstrumentationRegistry.getInstrumentation().targetContext.asApp()
        app.resetDI(listOf(scopesModule), listOf(mainScopeMock))
    }

    @After
    fun tearDown() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.asApp()
        app.resetDI(listOf(mainScopeMock), listOf(scopesModule))
    }

    @Test
    @FailTestOnLeak
    fun showErrorWhenLogoutReturnsError() {
        coEvery { logoutMock() } returns DataNotFound.left()
        coEvery { getProfileMock() } returns profile.right()

        val context = startActivity()
        BaristaClickInteractions.clickOn(R.id.logout)

        BaristaVisibilityAssertions.assertDisplayed(context.getString(R.string.unknown_error))
    }

    @Test
    @FailTestOnLeak
    fun activityShowsWelcomeMessageAndLogoutButton() {
        coEvery { getProfileMock() } returns profile.right()

        val context = startActivity()

        BaristaVisibilityAssertions.assertDisplayed(R.id.logout)
        BaristaVisibilityAssertions.assertDisplayed(context.getString(R.string.action_sign_out))
        BaristaVisibilityAssertions.assertDisplayed(R.id.message)
        BaristaVisibilityAssertions.assertDisplayed(context.getString(R.string.welcome) + username)
    }

    @Test
    @FailTestOnLeak
    fun activityShowsErrorIfProfileNotFound() {
        coEvery { getProfileMock() } returns DataNotFound.left()

        val context = startActivity()

        BaristaVisibilityAssertions.assertDisplayed(R.id.logout)
        BaristaVisibilityAssertions.assertDisplayed(context.getString(R.string.action_sign_out))
        BaristaVisibilityAssertions.assertDisplayed(R.id.message)
        BaristaVisibilityAssertions.assertDisplayed(context.getString(R.string.welcome))
        BaristaVisibilityAssertions.assertDisplayed(context.getString(R.string.unknown_error))
    }

    companion object {
        const val username = "john.doe"
        const val validEmail = "john.doe@company.com"
        const val token = "dsfdsfsdfdsgs"
        val profile = Profile(Email(validEmail), Token(token))
    }
}