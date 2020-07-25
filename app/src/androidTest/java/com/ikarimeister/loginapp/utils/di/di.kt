package com.ikarimeister.loginapp.utils.di

import android.app.Application
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

fun Application.resetDI(modulesToUnload: List<Module>, modulesToLoad: List<Module>) {
    unloadKoinModules(modulesToUnload)
    loadKoinModules(modulesToLoad)
}