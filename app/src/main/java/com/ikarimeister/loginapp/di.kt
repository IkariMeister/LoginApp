package com.ikarimeister.loginapp

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.ikarimeister.loginapp.data.ConfigurationRepository
import com.ikarimeister.loginapp.data.LoginApiClient
import com.ikarimeister.loginapp.data.local.ConfigurationDataSource
import com.ikarimeister.loginapp.data.local.JsonSerializer
import com.ikarimeister.loginapp.data.local.serializers.ProfileGsonSerializer
import com.ikarimeister.loginapp.data.local.sharedpreferences.SharedPreferencesProfileDataSource
import com.ikarimeister.loginapp.data.local.sharedpreferences.SharedPreferencesTokenDataSource
import com.ikarimeister.loginapp.data.network.FakeLoginApiClient
import com.ikarimeister.loginapp.data.repositories.ProfileRepository
import com.ikarimeister.loginapp.data.repositories.TokenRepository
import com.ikarimeister.loginapp.domain.model.Profile
import com.ikarimeister.loginapp.domain.model.Token
import com.ikarimeister.loginapp.domain.usecases.IsLoginStored
import com.ikarimeister.loginapp.domain.usecases.Login
import com.ikarimeister.loginapp.domain.usecases.Logout
import com.ikarimeister.loginapp.ui.activities.LoginActivity
import com.ikarimeister.loginapp.ui.activities.MainActivity
import com.ikarimeister.loginapp.ui.presenter.LoginPresenter
import com.ikarimeister.loginapp.ui.presenter.MainPresenter
import com.ikarimeister.loginapp.ui.view.LoginView
import com.ikarimeister.loginapp.ui.view.MainView
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
    single {
        androidApplication()
                .getSharedPreferences(LoginApp.sharedName, MODE_PRIVATE)
    }
    single { Gson() }
}

val dataModule = module {
    single<JsonSerializer<Profile>>(named("Profile")) { ProfileGsonSerializer(gson = get()) }
    single<ConfigurationDataSource<Token>>(named("Token")) {
        SharedPreferencesTokenDataSource(preferences = get())
    }
    single<ConfigurationRepository<Token>>(named("Token")) {
        TokenRepository(datasource = get(named("Token")))
    }
    single<ConfigurationDataSource<Profile>>(named("Profile")) {
        SharedPreferencesProfileDataSource(preferences = get(), jsonSerializer = get(named("Profile")))
    }
    single<ConfigurationRepository<Profile>>(named("Profile")) { ProfileRepository(datasource = get(named("Profile"))) }
    factory<LoginApiClient> { FakeLoginApiClient }
}

val scopesModule = module {
    scope(named<LoginActivity>()) {
        factory { (view: LoginView) ->
            LoginPresenter(
                    view = view,
                    login = get(),
                    isLoginStored = get(),
                    bgDispatcher = get(named("BG")),
                    uiDispacher = get(named("UI"))
            )
        }
        scoped { Login(repository = get(named("Token")), apiClient = get()) }
        scoped { IsLoginStored(repository = get(named("Token"))) }
    }
    scope(named<MainActivity>()) {
        factory { (view: MainView) ->
            MainPresenter(
                    view = view,
                    logout = get(),
                    bgDispatcher = get(named("BG")),
                    uiDispacher = get(named("UI"))
            )
        }
        scoped { Logout(repository = get(named("Token"))) }
    }
}