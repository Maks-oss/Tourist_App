package com.vkpi.touristapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkpi.touristapp.data.City
import com.vkpi.touristapp.data.Places
import com.vkpi.touristapp.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(val repository: PlaceRepository) : ViewModel() {
    private val _cityLiveData: MutableLiveData<City> = MutableLiveData()
    val cityLiveData: LiveData<City> = _cityLiveData

    private val _placeLiveData: MutableLiveData<Places> = MutableLiveData()
    val placesLiveData: LiveData<Places> = _placeLiveData

    fun applyCity(name:String){
        viewModelScope.launch {
            _cityLiveData.postValue(repository.getCity(name))
        }
    }

    fun applyPlaces(lat:String,lot:String){
        viewModelScope.launch {
            _placeLiveData.postValue(repository.getPlaces(lat,lot))
        }
    }
}