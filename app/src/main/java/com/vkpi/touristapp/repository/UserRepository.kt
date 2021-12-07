package com.vkpi.touristapp.repository

import com.vkpi.touristapp.database.dao.UserDao
import com.vkpi.touristapp.database.entities.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun getUserById(id: Long) = userDao.getUserById(id)


}