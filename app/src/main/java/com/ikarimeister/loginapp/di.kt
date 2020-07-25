package com.ikarimeister.loginapp

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.data.TokenRepository
import com.ikarimeister.loginapp.data.local.SharedPreferencesTokenDataSource
import com.ikarimeister.loginapp.data.local.TokenDataSource
import com.ikarimeister.loginapp.data.network.FakeLoginApiClient
import com.ikarimeister.loginapp.domain.usecases.IsLoginStored
import com.ikarimeister.loginapp.domain.usecases.Login
import com.ikarimeister.loginapp.domain.usecases.Logout
import com.ikarimeister.loginapp.ui.activities.LoginActivity
import com.ikarimeister.loginapp.ui.presenter.LoginPresenter
import com.ikarimeister.loginapp.ui.view.LoginView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(appModule, dataModule, scopesModule))
    }
}

val appModule = module {
    single<CoroutineDispatcher>(named("UI")) { Dispatchers.Main }
    single<CoroutineDispatcher>(named("BG")) { Dispatchers.IO }
}

val dataModule = module {
    factory<TokenDataSource> {
        SharedPreferencesTokenDataSource(androidApplication()
                .getSharedPreferences(SharedPreferencesTokenDataSource.ID, MODE_PRIVATE))
    }
    factory { TokenRepository(get()) }
    factory<LoginApiClient> { FakeLoginApiClient }
}

val scopesModule = module {
    scope(named<LoginActivity>()) {
        factory { (view: LoginView) ->
            LoginPresenter(view, get(), get(), get(named("BG")), get(named("UI")))
        }
        scoped { Login(get(), get()) }
        scoped { IsLoginStored(get()) }
    }
    scope(named("MainScreen")) {
        scoped { Logout(get()) }
    }
}