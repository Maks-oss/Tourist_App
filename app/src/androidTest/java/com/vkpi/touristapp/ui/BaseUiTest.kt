package com.vkpi.touristapp.ui

import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
abstract class BaseUiTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    open fun setup() {
        hiltRule.inject()
    }
}