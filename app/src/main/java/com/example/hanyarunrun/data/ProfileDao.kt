package com.example.hanyarunrun.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProfileDao {

    @Insert
    suspend fun insert(data: ProfileEntity)

    @Update
    suspend fun update(data: ProfileEntity)

    @Query("SELECT * FROM profile_table ORDER BY id DESC")
    fun getAll(): LiveData<List<ProfileEntity>>

    @Query("SELECT * FROM profile_table WHERE id = :dataId")
    suspend fun getById(dataId: Int): ProfileEntity?

    @Delete
    suspend fun delete(data: ProfileEntity)

    @Query("SELECT * FROM profile_table WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): ProfileEntity?

    @Query("UPDATE profile_table SET photoPath = :path WHERE id = 1")
    suspend fun updatePhotoPath(path: String)
}