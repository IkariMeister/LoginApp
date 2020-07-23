package com.ikarimeister.loginapp.ui.view

import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.ValidationErrors

interface LoginView {
    fun showLoading()
    fun hideLoading()
    fun showError(error: LoginError)
    fun showError(errors: List<ValidationErrors>)
    fun navigateToLoggedScreen()
    fun showLoginForm()
}