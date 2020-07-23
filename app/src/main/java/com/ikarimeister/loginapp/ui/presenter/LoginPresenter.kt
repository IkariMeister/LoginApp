package com.ikarimeister.loginapp.ui.presenter

import com.ikarimeister.loginapp.domain.model.Email
import com.ikarimeister.loginapp.domain.model.Password
import com.ikarimeister.loginapp.domain.model.User
import com.ikarimeister.loginapp.domain.usecases.IsLoginStored
import com.ikarimeister.loginapp.domain.usecases.Login
import com.ikarimeister.loginapp.ui.coomons.Scope
import com.ikarimeister.loginapp.ui.view.LoginView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPresenter(
    private val view: LoginView?,
    private val login: Login,
    private val isLoginStored: IsLoginStored,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    iuDispacher: CoroutineDispatcher = Dispatchers.Main
) : Scope by Scope.Impl(iuDispacher) {

    fun doLogin(email: Email, password: Password) = launch {
        view?.showLoading()
        val user = User(email, password)
        val login = withContext(ioDispatcher) { login(user) }
        view?.hideLoading()
        login.fold(
                { view?.showLoginError(it) },
                { view?.navigateToLoggedScreen() }
        )
    }

    fun onStart() = launch {
        view?.showLoading()
        val isLogged = withContext(ioDispatcher) { isLoginStored() }
        view?.hideLoading()
        isLogged.fold(
                { view?.showLoginForm() },
                { view?.navigateToLoggedScreen() }
        )
    }
}