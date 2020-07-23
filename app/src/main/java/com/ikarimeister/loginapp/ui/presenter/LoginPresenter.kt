package com.ikarimeister.loginapp.ui.presenter

import arrow.core.Nel
import com.ikarimeister.loginapp.domain.model.Email
import com.ikarimeister.loginapp.domain.model.Password
import com.ikarimeister.loginapp.domain.model.User
import com.ikarimeister.loginapp.domain.model.ValidationErrors
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
    private val bgDispatcher: CoroutineDispatcher = Dispatchers.IO,
    uiDispacher: CoroutineDispatcher = Dispatchers.Main
) : Scope by Scope.Impl(uiDispacher) {

    fun doLogin(email: Email, password: Password) {
        User.validate(email, password).fold(
                { errors: Nel<ValidationErrors> ->
                    view?.showError(errors.toList())
                },
                { user: User ->
                    doLogin(user)
                }
        )
    }

    private fun doLogin(user: User) = launch {
        view?.showLoading()
        val login = withContext(bgDispatcher) { login(user) }
        view?.hideLoading()
        login.fold(
                { view?.showError(it) },
                { view?.navigateToLoggedScreen() }
        )
    }

    fun onStart() = launch {
        view?.showLoading()
        val isLogged = withContext(bgDispatcher) { isLoginStored() }
        view?.hideLoading()
        isLogged.fold(
                { view?.showLoginForm() },
                { view?.navigateToLoggedScreen() }
        )
    }
}