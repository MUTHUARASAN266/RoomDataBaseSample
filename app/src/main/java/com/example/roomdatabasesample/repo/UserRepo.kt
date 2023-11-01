package com.example.roomdatabasesample.repo

import androidx.lifecycle.LiveData
import com.example.roomdatabasesample.dao.UserDao
import com.example.roomdatabasesample.model.UserData

class UserRepo(private val userDao: UserDao) {

    suspend fun insert(userData: UserData){
        userDao.insert(userData)
    }
    suspend fun update(userData: UserData){
        userDao.update(userData)
    }
    suspend fun delete(userData: UserData){
        userDao.delete(userData)
    }

    fun deleteByUserId(id:Int){
         userDao.deleteUserById(id)
    }

      fun getAllUserData():LiveData<List<UserData>>{
       return userDao.getAllData()
    }
}