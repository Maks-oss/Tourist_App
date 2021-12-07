package com.vkpi.touristapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkpi.touristapp.data.City
import com.vkpi.touristapp.database.entities.User
import com.vkpi.touristapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> = _userLiveData

    private val _userIdLiveData = MutableLiveData<Long>()
    val userIdLiveData: LiveData<Long> = _userIdLiveData

    suspend fun isUserExist(id: Long) = userRepository.getUserById(id) != null
    fun applyUserId(id: Long) {
        _userIdLiveData.value = id
    }


    fun insertUserIntoDb(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }
}