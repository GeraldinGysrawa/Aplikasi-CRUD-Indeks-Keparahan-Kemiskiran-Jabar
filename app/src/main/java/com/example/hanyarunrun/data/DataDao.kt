package com.example.hanyarunrun.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<DataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: DataEntity)

    @Update
    suspend fun update(data: DataEntity)

    @Delete
    suspend fun delete(data: DataEntity)

    @Query("DELETE FROM data_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM data_table ORDER BY tahun DESC")
    fun getAll(): LiveData<List<DataEntity>> //

    @Query("SELECT COUNT(*) FROM data_table")
    fun getTotalCount(): LiveData<Int>

    @Query("SELECT * FROM data_table WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): DataEntity?

    @Query("DELETE FROM data_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT tahun, COUNT(*) as jumlah FROM data_table GROUP BY tahun ORDER BY tahun")
    fun getDataByYear(): Flow<List<YearlyData>>
}

