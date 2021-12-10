package com.vkpi.touristapp.ui

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.vkpi.touristapp.R
import com.vkpi.touristapp.database.entities.Place
import com.vkpi.touristapp.di.launchFragmentInHiltContainer
import com.vkpi.touristapp.lists.SavedPlaceListViewHolder
import com.vkpi.touristapp.ui.fragments.SavedFragment
import com.vkpi.touristapp.ui.fragments.SavedFragmentDirections
import com.vkpi.touristapp.viewmodels.PlaceViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.Before
import org.junit.Test


@HiltAndroidTest
@MediumTest
class SavedFragmentTest : BaseUiTest() {
    private lateinit var navController: NavController
    @Before
    override fun setup(){
        hiltRule.inject()
        navController = mockkClass(
            NavController::class, relaxed = true
        )
        val placeViewModel = mockkClass(PlaceViewModel::class, relaxed = true)
        every { placeViewModel.deletePlaceFromDb(any()) } returns Unit
        launchFragmentInHiltContainer<SavedFragment> {
            Navigation.setViewNavController(requireView(), navController)
            this.placeViewModel = placeViewModel
            this.savedPlaceListAdapter.submitList(
                listOf(
                    Place(
                        "test",
                        1,
                        "test",
                        "test",
                        "test",
                        "test"
                    )
                )
            )
        }
    }
    @Test
    fun whenItemSwipedItShouldBeDeleted() {
        onView(withId(R.id.saved_place_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SavedPlaceListViewHolder>(
                0, swipeLeft()
            )
        )
        onView(withId(R.id.saved_place_list)).check(RecyclerViewItemCountAssertion(0))
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.deleted_item_message)))
    }

    @Test
    fun onItemClickShouldNavigateToItemDetail(){
        onView(withId(R.id.saved_place_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SavedPlaceListViewHolder>(
                0, click()
            )
        )
        verify {
            navController.navigate(SavedFragmentDirections.actionSavedFragmentToDetailedPlaceFragment("test"))
        }
    }
    private class RecyclerViewItemCountAssertion(private val expectedCount: Int) :
        ViewAssertion {
        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            assertThat(adapter!!.itemCount).isEqualTo(expectedCount)
        }

    }
}