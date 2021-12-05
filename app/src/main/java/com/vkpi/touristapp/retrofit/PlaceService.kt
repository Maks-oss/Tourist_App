package com.vkpi.touristapp.retrofit

import com.vkpi.touristapp.BuildConfig
import com.vkpi.touristapp.data.City
import com.vkpi.touristapp.data.Places
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("en/places/geoname")
    suspend fun getCityResponse(
        @Query("name") name: String,
        @Query("apikey") apiKey: String = BuildConfig.apikey
    ): Response<City>

    @GET("en/places/radius")
    suspend fun getPlacesResponse(
        @Query("radius") name: String = "1000",
        @Query("lon") lon: String,
        @Query("lat") lat: String,
//        @Query("limit") limit: String="5",
        @Query("apikey") apiKey: String = BuildConfig.apikey
    ): Response<Places>

}