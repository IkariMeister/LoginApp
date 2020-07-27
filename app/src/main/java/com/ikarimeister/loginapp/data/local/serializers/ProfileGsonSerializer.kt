package com.ikarimeister.loginapp.data.local.serializers

import com.google.gson.Gson
import com.ikarimeister.loginapp.data.local.JsonSerializer
import com.ikarimeister.loginapp.domain.model.Profile

class ProfileGsonSerializer(private val gson: Gson) : JsonSerializer<Profile> {
    override fun Profile.toJson(): String = gson.toJson(this)

    override fun String.fromJson(clazz: Class<Profile>): Profile = gson.fromJson(this, clazz)
}