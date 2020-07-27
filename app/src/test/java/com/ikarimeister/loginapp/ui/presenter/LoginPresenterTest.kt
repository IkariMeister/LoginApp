package com.ikarimeister.loginapp.ui.presenter

import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.domain.model.*
import com.ikarimeister.loginapp.domain.usecases.IsLoginStored
import com.ikarimeister.loginapp.domain.usecases.Login
import com.ikarimeister.loginapp.ui.view.LoginView
import com.ikarimeister.loginapp.utils.MotherObject.email
import com.ikarimeister.loginapp.utils.MotherObject.emptyEmail
import com.ikarimeister.loginapp.utils.MotherObject.emptyPassword
import com.ikarimeister.loginapp.utils.MotherObject.password
import com.ikarimeister.loginapp.utils.MotherObject.token
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginPresenterTest {

    @MockK
    lateinit var login: Login

    @MockK
    lateinit var islogged: IsLoginStored

    @MockK
    lateinit var view: LoginView
    private lateinit var presenter: LoginPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter = LoginPresenter(view, login, islogged, Dispatchers.Unconfined, Dispatchers.Unconfined)
        presenter.startPresenter()
    }

    @After
    fun tearDown() {
        presenter.stopPresenter()
    }

    @Test
    fun `View should show and hide loading and show an error when Login return error`() {
        givenAView()
        every { runBlocking { login(any()) } } returns IncorrectCredentials.left()

        presenter.doLogin(email, password)

        verifyOrder {
            view.showLoading()
            view.hideLoading()
            view.showError(IncorrectCredentials)
        }
    }

    @Test
    fun `View should show and hide loading and navigate to logged screen when Login return OK`() {
        givenAView()
        every { runBlocking { login(any()) } } returns token.right()

        presenter.doLogin(email, password)

        verifyOrder {
            view.showLoading()
            view.hideLoading()
            view.navigateToLoggedScreen()
        }
    }

    @Test
    fun `View should show and hide loading and navigate to logged screen when starts and a Token is stored`() {
        givenAView()
        every { runBlocking { islogged() } } returns token.right()

        presenter.onStart()

        verifyOrder {
            view.showLoading()
            view.hideLoading()
            view.navigateToLoggedScreen()
        }
    }

    @Test
    fun `View should show and hide loading and show logging form when starts and a Token is not stored`() {
        givenAView()
        every { runBlocking { islogged() } } returns DataNotFound.left()

        presenter.onStart()

        verifyOrder {
            view.showLoading()
            view.hideLoading()
            view.showLoginForm()
        }
    }

    @Test
    fun `View should show validation errors in login form when user is not valid`() {
        givenAView()

        presenter.doLogin(emptyEmail, emptyPassword)

        verify { view.showError(any<List<ValidationErrors>>()) }
    }

    private fun givenAView() {
        every { view.showLoading() } returns Unit
        every { view.hideLoading() } returns Unit
        every { view.showError(any<LoginError>()) } returns Unit
        every { view.showError(any<List<ValidationErrors>>()) } returns Unit
        every { view.navigateToLoggedScreen() } returns Unit
    }
}