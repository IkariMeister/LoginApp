package com.ikarimeister.loginapp.commons

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * Used as weakReference delegate for views not to leak the view
 * Taken from https://github.com/Karumi/KataSuperHeroesKotlin/
 */

fun <T> weak(value: T) = WeakRef(value)

class WeakRef<out T>(value: T) {
    private val weakReference: WeakReference<T> = WeakReference(value)
    operator fun getValue(thisRef: Any, property: KProperty<*>): T? = weakReference.get()
}