package com.vkpi.touristapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vkpi.touristapp.database.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE userId=:id")
    suspend fun getUserById(id:Long):User
}