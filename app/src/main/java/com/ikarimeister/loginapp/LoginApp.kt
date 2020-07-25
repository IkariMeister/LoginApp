package com.ikarimeister.loginapp

import android.app.Application
import android.content.Context

class LoginApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}

fun Context.asApp() = this.applicationContext as LoginApp