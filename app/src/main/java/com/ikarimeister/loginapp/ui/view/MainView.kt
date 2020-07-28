package com.ikarimeister.loginapp.ui.view

import com.ikarimeister.loginapp.domain.model.Profile
import com.ikarimeister.loginapp.domain.model.StorageError

interface MainView {
    fun showProfile(profile: Profile)
    fun showError(error: StorageError)
    fun navigateToLoginScreen()
}