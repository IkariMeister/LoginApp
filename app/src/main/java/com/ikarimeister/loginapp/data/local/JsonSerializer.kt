package com.ikarimeister.loginapp.data.local

interface JsonSerializer<T> {
    fun T.toJson(): String
    fun String.fromJson(clazz: Class<T>): T
}