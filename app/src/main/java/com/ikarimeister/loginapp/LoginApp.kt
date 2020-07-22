package com.ikarimeister.loginapp

import android.app.Application
import android.content.Context

class LoginApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: LoginApp
            private set
    }
}

fun Context.asApp(): LoginApp = this.applicationContext as LoginApp