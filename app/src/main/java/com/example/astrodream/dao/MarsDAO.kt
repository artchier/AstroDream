package com.example.astrodream.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.astrodream.entitiesDatabase.MarsRoom

@Dao
interface MarsDAO {
    @Insert
    suspend fun addMars(mars: MarsRoom)

    @Query("SELECT * FROM mars")
    suspend fun getAllMarsFavs(): List<MarsRoom>

    @Query("SELECT * FROM mars WHERE earth_date = :date")
    suspend fun getMarsAtDate(date: String): MarsRoom

    @Delete
    suspend fun deleteMars(mars: MarsRoom)
}