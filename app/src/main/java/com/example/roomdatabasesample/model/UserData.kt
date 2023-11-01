package com.example.roomdatabasesample.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "userid") val userid: Int?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "age")
    val age: Int?
)
