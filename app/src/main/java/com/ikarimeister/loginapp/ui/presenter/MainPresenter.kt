package com.ikarimeister.loginapp.ui.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ikarimeister.loginapp.commons.weak
import com.ikarimeister.loginapp.domain.usecases.GetProfile
import com.ikarimeister.loginapp.domain.usecases.Logout
import com.ikarimeister.loginapp.ui.coomons.Scope
import com.ikarimeister.loginapp.ui.view.MainView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainPresenter(
    view: MainView,
    private val getProfile: GetProfile,
    private val logout: Logout,
    private val bgDispatcher: CoroutineDispatcher,
    uiDispacher: CoroutineDispatcher
) : LifecycleObserver, Scope by Scope.Impl(uiDispacher) {

    private val view by weak(view)

    init {
        startPresenter()
    }

    fun doLogout() = launch {
        withContext(bgDispatcher) { logout() }.fold(
                { view?.showError(it) },
                { view?.navigateToLoginScreen() }
        )
    }

    fun recoverProfile() = launch {
        val profile = with(bgDispatcher) { getProfile() }
        profile.fold(
                { view?.showError(it) },
                { view?.showProfile(it) }
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startPresenter() {
        initScope()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopPresenter() {
        super.destroyScope()
    }
}