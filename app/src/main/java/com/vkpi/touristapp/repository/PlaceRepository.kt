package com.vkpi.touristapp.repository

import android.util.Log
import com.vkpi.touristapp.retrofit.PlaceService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceRepository @Inject constructor(private val placeService: PlaceService) {
    suspend fun getCity(name: String) = placeService.getCityResponse(name = name).body()

    suspend fun getPlaces(lat: String, lon: String) =
        placeService.getPlacesResponse(lat = lat, lon = lon).body()
}