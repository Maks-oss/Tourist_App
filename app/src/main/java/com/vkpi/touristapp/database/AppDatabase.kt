package com.vkpi.touristapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vkpi.touristapp.database.dao.PlaceDao
import com.vkpi.touristapp.database.dao.UserDao
import com.vkpi.touristapp.database.entities.Place
import com.vkpi.touristapp.database.entities.User

@Database(entities = [Place::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun placeDao(): PlaceDao
}