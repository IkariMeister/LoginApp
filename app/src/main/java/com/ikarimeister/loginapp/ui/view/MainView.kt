package com.ikarimeister.loginapp.ui.view

import com.ikarimeister.loginapp.domain.model.StorageError

interface MainView {
    fun showError(error: StorageError)
    fun navigateToLoginScreen()
}