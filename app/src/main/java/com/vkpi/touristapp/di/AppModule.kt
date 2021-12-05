package com.vkpi.touristapp.di

import com.vkpi.touristapp.list.PlaceListAdapter
import com.vkpi.touristapp.retrofit.PlaceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesBaseUrl() : String = "https://api.opentripmap.com/0.1/"

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL : String) : Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    @Provides
    @Singleton
    fun providePlaceService(retrofit : Retrofit) : PlaceService = retrofit.create(PlaceService::class.java)

    @Provides
    fun providesPlacesListAdapter() : PlaceListAdapter = PlaceListAdapter()
}