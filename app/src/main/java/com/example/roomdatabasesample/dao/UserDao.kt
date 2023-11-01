package com.example.roomdatabasesample.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.roomdatabasesample.model.UserData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(userData: UserData)

    @Update
     fun update(userData: UserData)

    @Delete
     fun delete(userData: UserData)

  /*  @Query("SELECT * FROM user order by userid  desc")
    fun getAllData(): kotlinx.coroutines.flow.Flow<List<UserData>>*/

    @Query("SELECT * FROM user")
     fun getAllData1(): Flow<List<UserData>>

    @Query("SELECT * FROM user")
     fun getAllData(): LiveData<List<UserData>>

    @Query("SELECT * FROM user WHERE userid = :userId")
     fun getUserById(userId: Int): UserData

    @Query("DELETE FROM user WHERE userid = :userId")
     fun deleteUserById(userId: Int)
}
