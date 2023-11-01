package com.example.roomdatabasesample.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdatabasesample.model.UserData
import com.example.roomdatabasesample.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepo: UserRepo): ViewModel() {

    val getData: LiveData<List<UserData>> = userRepo.getAllUserData()

    fun insert(userData: UserData) {
        CoroutineScope(Dispatchers.IO).launch() {
            userRepo.insert(userData)
        }
    }
    fun deleteByUserId(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            userRepo.deleteByUserId(id)
        }
    }
}