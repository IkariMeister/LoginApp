package com.ikarimeister.loginapp

import android.app.Application
import android.content.Context

class LoginApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    companion object {
        const val dbName = "login-db"
        const val sharedName = "login-app"
    }
}

fun Context.asApp() = this.applicationContext as LoginApp