package com.vkpi.touristapp.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vkpi.touristapp.database.AppDatabase
import com.vkpi.touristapp.database.dao.PlaceDao
import com.vkpi.touristapp.database.dao.UserDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
open class DaoTests {
    protected lateinit var placeDao: PlaceDao
    protected lateinit var userDao: UserDao
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase


}