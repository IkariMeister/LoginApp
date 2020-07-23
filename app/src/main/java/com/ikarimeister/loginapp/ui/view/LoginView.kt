package com.ikarimeister.loginapp.ui.view

import com.ikarimeister.loginapp.domain.model.LoginError

interface LoginView {
    fun showLoading()
    fun hideLoading()
    fun showLoginError(error: LoginError)
    fun navigateToLoggedScreen()
}