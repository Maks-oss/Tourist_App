package com.vkpi.touristapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vkpi.touristapp.MainCoroutineRule
import com.vkpi.touristapp.database.entities.User
import com.vkpi.touristapp.livedatautils.getOrAwaitValue
import com.vkpi.touristapp.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: UserViewModel

    @MockK
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = UserViewModel(repository)
    }

    @Test
    fun `when user is inserted, his id livedata should be returned`() =
        mainCoroutineRule.runBlockingTest {
            val user=User(1,"test","test")
            coEvery { repository.insertUser(any()) } returns  viewModel.applyUserId(1)
            viewModel.insertUserIntoDb(user)
            assertThat(viewModel.userIdLiveData.getOrAwaitValue()).isEqualTo(1)
        }

    @Test
    fun `User isExist must return True`() =
        mainCoroutineRule.runBlockingTest {
            val user=User(1,"test","test")
            coEvery { repository.insertUser(any()) } returns  viewModel.applyUserId(1)
            coEvery { repository.getUserById(1) } returns user
            viewModel.insertUserIntoDb(user)
            val isExists=viewModel.isUserExist(1)
            assertThat(isExists).isTrue()
        }

}