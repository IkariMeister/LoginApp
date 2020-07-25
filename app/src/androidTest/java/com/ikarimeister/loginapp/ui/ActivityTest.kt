package com.ikarimeister.loginapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
abstract class ActivityTest<T : Activity>(clazz: Class<T>) {
    @Rule
    @JvmField
    val testRule: IntentsTestRule<T> = IntentsTestRule(clazz, true, false)
    @Rule
    @JvmField
    val activityRule: ActivityTestRule<T> = ActivityTestRule(clazz)

    fun startActivity(args: Bundle = Bundle()): T {
        val intent = Intent()
        intent.putExtras(args)
        return testRule.launchActivity(intent)
    }
}