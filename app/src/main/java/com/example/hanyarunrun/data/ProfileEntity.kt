package com.example.hanyarunrun.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class ProfileEntity(
    @PrimaryKey val id: Int = 1,
    var username: String = "Gerald",
    var uid: String = "231511011",
    var email: String = "geraldin@gmail.com",

)
