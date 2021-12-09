package com.vkpi.touristapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat
import com.vkpi.touristapp.DataSupplier.getFakePlaceDetail
import com.vkpi.touristapp.DataSupplier.getFeature
import com.vkpi.touristapp.MainCoroutineRule
import com.vkpi.touristapp.data.*
import com.vkpi.touristapp.database.entities.Place
import com.vkpi.touristapp.livedatautils.getOrAwaitValue
import com.vkpi.touristapp.repository.PlaceRepository
import com.vkpi.touristapp.repository.UserRepository
import com.vkpi.touristapp.utils.getUserPlaceEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlaceViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: PlaceViewModel

    @MockK
    private lateinit var repository: PlaceRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = PlaceViewModel(repository)
    }

    @Test
    fun `apply city should apply city to livedata`() {
        val city = City("test", 0.0, 0.0, "name", 0, "test")
        coEvery { repository.getCity(any()) } returns city
        viewModel.applyCity("name")
        val value = viewModel.cityLiveData.getOrAwaitValue()
        assertThat(value).isEqualTo(city)
    }

    @Test
    fun `insert user place into db should apply user place to livedata`() {
        val place = Place("test", 1, "test", "test", "test", "test")
        val mutableLiveData = MutableLiveData<List<Place>>()
        coEvery { repository.insertPlace(any()) } returns mutableLiveData.postValue(listOf(place))
        coEvery { repository.getUserPlaces(any()) } returns listOf(place)
        viewModel.insertPlaceIntoDb(place)
        viewModel.applyUserPlaces(1)
        val value = viewModel.userPlacesLiveData.getOrAwaitValue()
        assertThat(value).isEqualTo(mutableLiveData.value)
    }

    @Test
    fun `delete user place from db should delete user place from livedata`() {
        val place = Place("test", 1, "test", "test", "test", "test")
        val mutableLiveData = MutableLiveData<List<Place>>()
        coEvery { repository.insertPlace(any()) } returns mutableLiveData.postValue(listOf(place))
        coEvery { repository.deletePlace(any()) } returns mutableLiveData.postValue(listOf())
        coEvery { repository.getUserPlaces(any()) } returns listOf()
        viewModel.insertPlaceIntoDb(place)
        viewModel.deletePlaceFromDb(place)
        viewModel.applyUserPlaces(1)
        val value = viewModel.userPlacesLiveData.getOrAwaitValue()
        assertThat(value).isEmpty()
    }

    @Test
    fun `apply place detail from server should apply place detal to livedata`() {
        val placeDetail = getFakePlaceDetail()
        coEvery { repository.getPlaceDetail(any()) } returns placeDetail
        viewModel.applyPlaceDetailFromServer("test", 1)
        val value = viewModel.placeDetailLiveData.getOrAwaitValue()
        assertThat(value).isEqualTo(placeDetail.getUserPlaceEntity(1))
    }

    @Test
    fun `apply places should apply places to livedata`() = runBlocking {
        val places = Places(listOf(), "test")
        coEvery { repository.getPlaces(any(), any(), any()) } returns places
        viewModel.applyPlaces("test", "test")
        val value = viewModel.placesLiveData.getOrAwaitValue().data
        assertThat(value).isEqualTo(places)

    }

    @Test
    fun `get place id by location should return id`() = runBlocking {
        val places = Places(
            features = listOf(getFeature(2,"test")), "test")
        coEvery { repository.getPlaces(any(), any(), any()) } returns places
        viewModel.applyPlaces("test", "test")
        val value = viewModel.getPlaceIdByLocation(LatLng(0.0, 0.0))
        assertThat(value).isEqualTo("test")
    }

    @Test
    fun `get sorted list should return list with sorted rates and distinct`() = runBlocking {
        val places = Places(
            features = listOf(getFeature(2,"test"),getFeature(5,""),getFeature(6,"test")), "test")
        coEvery { repository.getPlaces(any(), any(), any()) } returns places
        viewModel.applyPlaces("test", "test")
        val value = viewModel.getSortedFeatureList()
        assertThat(value).isEqualTo(listOf(getFeature(6,"test"),getFeature(5,"")))
    }


}