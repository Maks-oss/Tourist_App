package com.vkpi.touristapp.database.dao

import androidx.room.*
import com.vkpi.touristapp.database.entities.Place
import com.vkpi.touristapp.database.entities.User
import com.vkpi.touristapp.database.entities.UserPlaces

@Dao
interface PlaceDao {
    @Transaction
    @Query("SELECT * FROM User")
    suspend fun getUserPlaces():List<UserPlaces>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: Place)

    @Query("SELECT * FROM Place WHERE placeId=:id")
    suspend fun getPlaceById(id:String): Place

    @Delete
    suspend fun deletePlace(place: Place)
}