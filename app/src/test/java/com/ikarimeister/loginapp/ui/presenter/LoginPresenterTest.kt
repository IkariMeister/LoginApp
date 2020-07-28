package com.ikarimeister.loginapp.ui.presenter

import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.domain.model.DataNotFound
import com.ikarimeister.loginapp.domain.model.IncorrectCredentials
import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.ValidationErrors
import com.ikarimeister.loginapp.domain.usecases.GetProfile
import com.ikarimeister.loginapp.domain.usecases.Login
import com.ikarimeister.loginapp.ui.view.LoginView
import com.ikarimeister.loginapp.utils.MotherObject.email
import com.ikarimeister.loginapp.utils.MotherObject.emptyEmail
import com.ikarimeister.loginapp.utils.MotherObject.emptyPassword
import com.ikarimeister.loginapp.utils.MotherObject.password
import com.ikarimeister.loginapp.utils.MotherObject.profile
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginPresenterTest {

    @MockK
    lateinit var login: Login

    @MockK
    lateinit var islogged: GetProfile

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
        coEvery { login(any()) } returns IncorrectCredentials.left()

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
        coEvery { login(any()) } returns profile.right()

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
        coEvery { islogged() } returns profile.right()

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
        coEvery { islogged() } returns DataNotFound.left()

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