package com.vkpi.touristapp.di

import android.content.Context
import androidx.room.Room
import com.vkpi.touristapp.database.AppDatabase
import com.vkpi.touristapp.database.dao.PlaceDao
import com.vkpi.touristapp.database.dao.UserDao
import com.vkpi.touristapp.retrofit.PlaceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Tourism"
        ).fallbackToDestructiveMigration().build()
    }
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
    @Provides
    fun providePlaceDao(appDatabase: AppDatabase): PlaceDao {
        return appDatabase.placeDao()
    }

}