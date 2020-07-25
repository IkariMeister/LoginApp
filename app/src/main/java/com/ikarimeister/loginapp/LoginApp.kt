package com.ikarimeister.loginapp

import android.app.Application

class LoginApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}