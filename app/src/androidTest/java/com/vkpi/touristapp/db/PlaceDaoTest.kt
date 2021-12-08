package com.vkpi.touristapp.db

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.vkpi.touristapp.database.dao.PlaceDao
import com.vkpi.touristapp.database.entities.Place
import com.vkpi.touristapp.database.entities.User
import com.vkpi.touristapp.database.entities.UserPlaces
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class PlaceDaoTest : DaoTests() {

    private lateinit var fakePlace: Place

    @Before
    fun setup() {
        hiltRule.inject()
        placeDao = database.placeDao()
        fakePlace = Place(
            "test id", 1, "test name", "test kinds", "test url", "testDesrciption"
        )
    }

    @Test
    fun insertUserPlaceShouldContainPlaceInDb() = runBlockingTest {
        userDao = database.userDao()
        val fakeUser = User(1, "fake login", "fake password")
        userDao.insertUser(fakeUser)
        placeDao.insertPlace(fakePlace)
        assertThat(placeDao.getUserPlaces()).contains(UserPlaces(fakeUser, listOf(fakePlace)))
    }

    @Test
    fun insertPlaceShouldReturnPlaceFromDb() = runBlockingTest {
        placeDao.insertPlace(fakePlace)
        assertThat(placeDao.getPlaceById(fakePlace.placeId)).isEqualTo(fakePlace)
    }

    @Test
    fun deletePlaceShouldNotContainPlaceInDb() = runBlockingTest {
        placeDao.insertPlace(fakePlace)
        placeDao.deletePlace(fakePlace)
        assertThat(placeDao.getPlaceById(fakePlace.placeId)).isNull()
    }

    @Test
    fun getPlaceByIdShouldReturnPlaceFromDb() = runBlockingTest {
        placeDao.insertPlace(fakePlace)
        assertThat(placeDao.getPlaceById(fakePlace.placeId)).isNotNull()
    }

    @Test
    fun getPlaceByIdShouldReturnNullFromDb() = runBlockingTest {
        assertThat(placeDao.getPlaceById(fakePlace.placeId)).isNull()
    }


    @After
    fun tearDown() {
        database.close()
    }
}