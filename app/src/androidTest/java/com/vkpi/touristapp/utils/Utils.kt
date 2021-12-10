package com.vkpi.touristapp.utils

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description

fun hasErrorText() =
    object : BoundedMatcher<View?, TextInputLayout>(TextInputLayout::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has error text: ")
        }

        override fun matchesSafely(view: TextInputLayout) = view.error != null
    }