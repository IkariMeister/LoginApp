package com.ikarimeister.loginapp.ui.presenter

import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.domain.model.*
import com.ikarimeister.loginapp.domain.usecases.Login
import com.ikarimeister.loginapp.ui.view.LoginView
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
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
    lateinit var view: LoginView
    private lateinit var presenter: LoginPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter = LoginPresenter(view, login, Dispatchers.Unconfined, Dispatchers.Unconfined)
        presenter.initScope()
    }

    @After
    fun tearDown() {
        presenter.destroyScope()
    }

    @Test
    fun `View should show and hide loading and show an error when Login return error`() {
        givenAView()
        every { runBlocking { login(any()) } } returns IncorrectCredentials.left()

        presenter.doLogin(email, password)

        verifyOrder {
            view.showLoading()
            view.hideLoading()
            view.showLoginError(IncorrectCredentials)
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

    private fun givenAView() {
        every { view.showLoading() } returns Unit
        every { view.hideLoading() } returns Unit
        every { view.showLoginError(any()) } returns Unit
        every { view.navigateToLoggedScreen() } returns Unit
    }

    companion object {
        private val email = Email("john.doe@company.com")
        private val password = Password("123456")
        private val token = Token("fdskjflsdjflsdjf")
        private val user = User(email, password)
    }
}