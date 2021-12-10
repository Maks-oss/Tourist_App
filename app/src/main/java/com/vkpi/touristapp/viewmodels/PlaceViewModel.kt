package com.vkpi.touristapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.vkpi.touristapp.data.City
import com.vkpi.touristapp.data.Feature
import com.vkpi.touristapp.data.PlaceDetail
import com.vkpi.touristapp.data.Places
import com.vkpi.touristapp.database.entities.Place
import com.vkpi.touristapp.repository.PlaceRepository
import com.vkpi.touristapp.utils.IMAGE_NOT_FOUND_URL
import com.vkpi.touristapp.utils.Resource
import com.vkpi.touristapp.utils.getUserPlaceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(private val placeRepository: PlaceRepository) :
    ViewModel() {
    private val _cityLiveData: MutableLiveData<City> = MutableLiveData()
    val cityLiveData: LiveData<City> = _cityLiveData

    private val _placeDetailLiveData: MutableLiveData<Place> = MutableLiveData()
    val placeDetailLiveData: LiveData<Place> = _placeDetailLiveData

    private val _placeLiveData: MutableLiveData<Resource<Places?>> = MutableLiveData()
    val placesLiveData: LiveData<Resource<Places?>> = _placeLiveData

    private val _userPlacesLiveData: MutableLiveData<List<Place>> = MutableLiveData()
    val userPlacesLiveData: LiveData<List<Place>> = _userPlacesLiveData

    fun applyUserPlaces(id: Long) {
        viewModelScope.launch {
            _userPlacesLiveData.postValue(placeRepository.getUserPlaces(id))
        }
    }

    fun insertPlaceIntoDb(place: Place) {
        viewModelScope.launch {
            placeRepository.insertPlace(place)
        }
    }

    fun deletePlaceFromDb(place: Place) {
        viewModelScope.launch {
            placeRepository.deletePlace(place)
        }
    }

    fun applyCity(name: String) {
        viewModelScope.launch {
            _cityLiveData.postValue(placeRepository.getCity(name))
        }
    }

    fun applyPlaceDetailFromServer(id: String,userId:Long) {
        viewModelScope.launch {
            _placeDetailLiveData.value = placeRepository.getPlaceDetail(id)?.getUserPlaceEntity(userId)
        }
    }
    fun applyPlaceDetailFromDatabase(id: String) {
        viewModelScope.launch {
            _placeDetailLiveData.value = placeRepository.getPlaceById(id)
        }
    }

    fun getCityName() = _cityLiveData.value?.name

    fun getPlaceIdByLocation(latLng: LatLng): String? {
        return _placeLiveData.value?.data?.features?.find {
            it.geometry.coordinates.contains(latLng.latitude) && it.geometry.coordinates.contains(
                latLng.longitude
            )
        }?.properties?.xid
    }

    fun getSortedFeatureList(): List<Feature>? {
        return _placeLiveData.value?.data?.features?.sortedByDescending { it.properties.rate }
            ?.distinctBy { it.properties.name }?.filter { it.properties.name.isNotEmpty() }
    }

    fun applyPlaces(lat: String, lot: String,radius:String = "1000") {
        _placeLiveData.value = Resource.Loading()
        viewModelScope.launch {
            delay(1000)
            try {
                _placeLiveData.postValue(Resource.Success(placeRepository.getPlaces(lat, lot,radius)))
            } catch (exc: Exception) {
                _placeLiveData.postValue(Resource.Error("Something went wrong"))
            }
        }
    }
}