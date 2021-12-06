package com.vkpi.touristapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkpi.touristapp.data.City
import com.vkpi.touristapp.data.PlaceDetail
import com.vkpi.touristapp.data.Places
import com.vkpi.touristapp.repository.PlaceRepository
import com.vkpi.touristapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(val repository: PlaceRepository) : ViewModel() {
    private val _cityLiveData: MutableLiveData<City> = MutableLiveData()
    val cityLiveData: LiveData<City> = _cityLiveData

    private val _placeDetailLiveData: MutableLiveData<PlaceDetail> = MutableLiveData()
    val placeDetailLiveData: LiveData<PlaceDetail> = _placeDetailLiveData

    private val _placeLiveData: MutableLiveData<Resource<Places?>> = MutableLiveData()
    val placesLiveData: LiveData<Resource<Places?>> = _placeLiveData

    fun applyCity(name: String) {
        viewModelScope.launch {
            _cityLiveData.postValue(repository.getCity(name))
        }
    }
    fun applyPlaceDetail(id:String){
        viewModelScope.launch {
            _placeDetailLiveData.value=repository.getPlaceDetail(id)
        }
    }
    fun applyPlaces(lat: String, lot: String) {
        _placeLiveData.value = Resource.Loading()
        viewModelScope.launch {
            delay(1000)
            try {
                _placeLiveData.postValue(Resource.Success(repository.getPlaces(lat, lot)))
            } catch (exc: Exception) {
                _placeLiveData.postValue(Resource.Error("Something went wrong"))
            }
        }
    }
}