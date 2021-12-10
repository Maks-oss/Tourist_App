package com.vkpi.touristapp.ui

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.vkpi.touristapp.R
import com.vkpi.touristapp.database.dao.UserDao
import com.vkpi.touristapp.di.launchFragmentInHiltContainer
import com.vkpi.touristapp.ui.fragments.LoginFragment
import com.vkpi.touristapp.ui.fragments.LoginFragmentDirections
import com.vkpi.touristapp.ui.fragments.RegistrationFragment
import com.vkpi.touristapp.ui.fragments.RegistrationFragmentDirections
import com.vkpi.touristapp.utils.hasErrorText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


@HiltAndroidTest
@MediumTest
class RegistrationFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testRegistrationFragmentToSearchNavigation() {
        val navController= Mockito.mock(NavController::class.java)
        val daoMock=Mockito.mock(UserDao::class.java)

        launchFragmentInHiltContainer<RegistrationFragment> {
            Navigation.setViewNavController(requireView(),navController)
        }
        Espresso.onView(ViewMatchers.withId(R.id.login_input)).perform(ViewActions.typeText("test12"))
        Espresso.onView(ViewMatchers.withId(R.id.password_input)).perform(ViewActions.typeText("test"))
        Espresso.onView(ViewMatchers.withId(R.id.password_repaet_nput)).perform(ViewActions.typeText("test"))
        Espresso.onView(ViewMatchers.withId(R.id.btn_login)).perform(ViewActions.click())
        Mockito.verify(navController).navigate(RegistrationFragmentDirections.actionRegistrationFragmentToSearchFragment())
    }

    @Test
    fun testRegistrationFragmentTextInputErrorsWhenEmptyInput(){
        launchFragmentInHiltContainer<RegistrationFragment> {}
        Espresso.onView(ViewMatchers.withId(R.id.btn_login)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.login_layout))
            .check(ViewAssertions.matches(hasErrorText()))
        Espresso.onView(ViewMatchers.withId(R.id.password_layout))
            .check(ViewAssertions.matches(hasErrorText()))
    }
    @Test
    fun testRegistrationFragmentLoginInputInputErrorWhenLengthLessThan4(){
        launchFragmentInHiltContainer<RegistrationFragment> {}
        Espresso.onView(ViewMatchers.withId(R.id.login_input)).perform(ViewActions.typeText("tes"))
        Espresso.onView(ViewMatchers.withId(R.id.password_input))
            .perform(ViewActions.typeText("test"))
        Espresso.onView(ViewMatchers.withId(R.id.btn_login)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.login_layout))
            .check(ViewAssertions.matches(hasErrorText()))
    }
    @Test
    fun testRegistrationFragmentPasswordInputInputErrorWhenLengthLessThan4(){
        launchFragmentInHiltContainer<RegistrationFragment> {}
        Espresso.onView(ViewMatchers.withId(R.id.login_input)).perform(ViewActions.typeText("test"))
        Espresso.onView(ViewMatchers.withId(R.id.password_input))
            .perform(ViewActions.typeText("tes"))
        Espresso.onView(ViewMatchers.withId(R.id.btn_login)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.password_layout))
            .check(ViewAssertions.matches(hasErrorText()))
    }
    @Test
    fun testRegistrationFragmentRepeatPasswordInputError(){
        launchFragmentInHiltContainer<RegistrationFragment> {}
        Espresso.onView(ViewMatchers.withId(R.id.login_input)).perform(ViewActions.typeText("test"))
        Espresso.onView(ViewMatchers.withId(R.id.password_input))
            .perform(ViewActions.typeText("test"))
        Espresso.onView(ViewMatchers.withId(R.id.password_repaet_nput))
            .perform(ViewActions.typeText("tests"))
        Espresso.onView(ViewMatchers.withId(R.id.btn_login)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.repeat_password_layout))
            .check(ViewAssertions.matches(hasErrorText()))
    }
}