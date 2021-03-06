package com.ikarimeister.loginapp.ui.presenter

import arrow.core.left
import arrow.core.right
import com.ikarimeister.loginapp.domain.model.DataNotFound
import com.ikarimeister.loginapp.domain.usecases.GetProfile
import com.ikarimeister.loginapp.domain.usecases.Logout
import com.ikarimeister.loginapp.ui.view.MainView
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainPresenterTest {

    @MockK
    lateinit var logout: Logout
    @MockK
    lateinit var getProfile: GetProfile

    @MockK
    lateinit var view: MainView
    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter = MainPresenter(view, getProfile, logout, Dispatchers.Unconfined, Dispatchers.Unconfined)
        presenter.startPresenter()
    }

    @After
    fun tearDown() {
        presenter.stopPresenter()
    }

    @Test
    fun `View should navigate to login screen if logout is Right`() {
        coEvery { logout() } returns Unit.right()

        presenter.doLogout()

        verify { view.navigateToLoginScreen() }
    }
    @Test
    fun `View should show error when logout is Left`() {
        coEvery { logout() } returns DataNotFound.left()

        presenter.doLogout()

        verify { view.showError(DataNotFound) }
    }
}