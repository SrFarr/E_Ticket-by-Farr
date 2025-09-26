package com.tiket.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user"
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val email:String,
    val username:String,
    val password:String,
    val isAdmin: Boolean = false
)
