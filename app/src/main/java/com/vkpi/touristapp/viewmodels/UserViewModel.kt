package com.vkpi.touristapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkpi.touristapp.data.City
import com.vkpi.touristapp.database.entities.User
import com.vkpi.touristapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData:LiveData<User?> = _userLiveData

    fun applyUser(id:Long){
        viewModelScope.launch {
            _userLiveData.postValue(userRepository.getUserById(id))
        }
    }
    fun applyUser(user:User){
        _userLiveData.value=user
    }

    fun insertUserIntoDb(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }
}