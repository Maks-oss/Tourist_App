package com.vkpi.touristapp.ui

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
//import com.android.dx.mockito.inline.extended.ExtendedMockito.*
import com.vkpi.touristapp.R
import com.vkpi.touristapp.di.launchFragmentInHiltContainer
import com.vkpi.touristapp.ui.fragments.LoginFragment
import com.vkpi.touristapp.ui.fragments.LoginFragmentDirections
import com.vkpi.touristapp.utils.hasErrorText
import com.vkpi.touristapp.viewmodels.UserViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

@HiltAndroidTest
@MediumTest
class LoginFragmentTest:BaseUiTest() {

    @Test
    fun testLoginFragmentNavigationWhenUserNotExists() {
        launchFragmentInHiltContainer<LoginFragment> {}
        onView(withId(R.id.login_input)).perform(typeText("test222"))
        onView(withId(R.id.password_input)).perform(typeText("test222"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.user_not_exist_message)))
    }
    @Test
    fun testLoginFragmentToRegistrationNavigation() {
        val navController = mockkClass(
            NavController::class,relaxed = true)
        launchFragmentInHiltContainer<LoginFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.btn_register)).perform(click())
        verify {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }
    }

    @Test
    fun testLoginFragmentTextInputErrorsWhenEmptyInput() {
        launchFragmentInHiltContainer<LoginFragment> {}
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.login_layout)).check(matches(hasErrorText()))
        onView(withId(R.id.password_layout)).check(matches(hasErrorText()))
    }

    @Test
    fun testLoginFragmentLoginInputInputErrorWhenLengthLessThan4() {
        launchFragmentInHiltContainer<LoginFragment> {}
        onView(withId(R.id.login_input)).perform(typeText("tes"))
        onView(withId(R.id.password_input)).perform(typeText("test"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.login_layout)).check(matches(hasErrorText()))
    }

    @Test
    fun testLoginFragmentPasswordInputInputErrorWhenLengthLessThan4() {
        launchFragmentInHiltContainer<LoginFragment> {}
        onView(withId(R.id.login_input)).perform(typeText("test"))
        onView(withId(R.id.password_input)).perform(typeText("tes"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.password_layout)).check(matches(hasErrorText()))
    }



    @Test
    fun testLoginFragmentNavigationWhenUserExists()  {
        val userViewModel = mockkClass(UserViewModel::class,relaxed = true)
        val navController = mockkClass(NavController::class,relaxed = true)
        coEvery { userViewModel.isUserExist(any()) }.returns(true)
        every { userViewModel.applyUserId(any()) }.returns(Unit)
        launchFragmentInHiltContainer<LoginFragment> {
            Navigation.setViewNavController(requireView(), navController)
            this.userViewModel = userViewModel
        }

        onView(withId(R.id.login_input)).perform(typeText("test222"))
        onView(withId(R.id.password_input)).perform(typeText("test222"))
        onView(withId(R.id.btn_login)).perform(click())
       verify {
           navController.navigate(LoginFragmentDirections.actionLoginFragmentToSearchFragment())
       }
    }

}