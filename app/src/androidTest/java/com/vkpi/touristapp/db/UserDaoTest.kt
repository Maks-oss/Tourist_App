package com.vkpi.touristapp.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.vkpi.touristapp.database.AppDatabase
import com.vkpi.touristapp.database.dao.UserDao
import com.vkpi.touristapp.database.entities.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class UserDaoTest:DaoTests() {
    private lateinit var fakeUser:User

    @Before
    fun setup() {
        hiltRule.inject()
        userDao = database.userDao()
        fakeUser = User(1, "fake login", "fake password")
    }

    @Test
    fun insertUserShouldReturnUserFromDb() = runBlockingTest {
        userDao.insertUser(fakeUser)
        assertThat(userDao.getUserById(1)).isNotNull()
    }
    @Test
    fun getUserByIdShouldReturnNull() = runBlockingTest {
        assertThat(userDao.getUserById(1)).isNull()
    }

    @After
    fun tearDown() {
        database.close()
    }
}