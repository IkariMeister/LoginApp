package com.ikarimeister.loginapp.utils.matcher

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class IsTextInputLayoutShowingErrorMatcher(val isShowing: Boolean = true) : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {}

    override fun matchesSafely(item: View?): Boolean {
        if (item !is TextInputLayout) return false
        return if (isShowing) item.error != null else item.error == null
    }
}