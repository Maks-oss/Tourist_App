package com.vkpi.touristapp.ui

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.android.material.textfield.TextInputLayout
import com.vkpi.touristapp.R
import com.vkpi.touristapp.database.dao.UserDao
import com.vkpi.touristapp.database.entities.User
import com.vkpi.touristapp.di.launchFragmentInHiltContainer
import com.vkpi.touristapp.ui.fragments.LoginFragment
import com.vkpi.touristapp.ui.fragments.LoginFragmentDirections
import com.vkpi.touristapp.ui.fragments.SavedFragment
import com.vkpi.touristapp.utils.hasErrorText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.any
import org.mockito.Mockito.verify

@HiltAndroidTest
@MediumTest
class LoginFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setup() {
        hiltRule.inject()
    }


    @Test
    fun testLoginFragmentToRegistrationNavigation() {
        val navController=Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<LoginFragment> {
            Navigation.setViewNavController(requireView(),navController)
        }
        onView(withId(R.id.btn_register)).perform(click())
        verify(navController).navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
    }

    @Test
    fun testLoginFragmentTextInputErrorsWhenEmptyInput(){
        launchFragmentInHiltContainer<LoginFragment> {}
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.login_layout)).check(matches(hasErrorText()))
        onView(withId(R.id.password_layout)).check(matches(hasErrorText()))
    }
    @Test
    fun testLoginFragmentLoginInputInputErrorWhenLengthLessThan4(){
        launchFragmentInHiltContainer<LoginFragment> {}
        onView(withId(R.id.login_input)).perform(typeText("tes"))
        onView(withId(R.id.password_input)).perform(typeText("test"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.login_layout)).check(matches(hasErrorText()))
    }
    @Test
    fun testLoginFragmentPasswordInputInputErrorWhenLengthLessThan4(){
        launchFragmentInHiltContainer<LoginFragment> {}
        onView(withId(R.id.login_input)).perform(typeText("test"))
        onView(withId(R.id.password_input)).perform(typeText("tes"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.password_layout)).check(matches(hasErrorText()))
    }
    @Test
    fun testLoginFragmentNavigationWhenUserNotExists(){
        launchFragmentInHiltContainer<LoginFragment> {}
        onView(withId(R.id.login_input)).perform(typeText("test"))
        onView(withId(R.id.password_input)).perform(typeText("test"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.user_not_exist_message)))
    }

}