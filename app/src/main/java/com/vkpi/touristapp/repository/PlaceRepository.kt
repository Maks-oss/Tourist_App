package com.vkpi.touristapp.repository

import android.util.Log
import com.vkpi.touristapp.database.AppDatabase
import com.vkpi.touristapp.database.dao.PlaceDao
import com.vkpi.touristapp.database.entities.Place
import com.vkpi.touristapp.database.entities.User
import com.vkpi.touristapp.retrofit.PlaceService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceRepository @Inject constructor(
    private val placeService: PlaceService,
    private val placeDao: PlaceDao
) {
    suspend fun insertPlace(place: Place) = placeDao.insertPlace(place)
    suspend fun deletePlace(place: Place) = placeDao.deletePlace(place)
    suspend fun getUserPlaces(id:Long) = placeDao.getUserPlaces().find {
        it.user.userId == id
    }?.places
    suspend fun getPlaceById(id:String) = placeDao.getPlaceById(id)
    suspend fun getCity(name: String) = placeService.getCityResponse(name = name).body()

    suspend fun getPlaces(lat: String, lon: String) =
        placeService.getPlacesResponse(lat = lat, lon = lon).body()

    suspend fun getPlaceDetail(xid: String) = placeService.getPlaceDetailResponse(xid).body()
}