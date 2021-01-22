package com.example.astrodream.dao

import androidx.room.*
import com.example.astrodream.entitiesDatabase.AllPicsFromDate
import com.example.astrodream.entitiesDatabase.MarsPicRoom
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

    @Insert
    suspend fun addMarsPic(marsPic: MarsPicRoom)

    @Transaction
    @Query("SELECT * FROM mars WHERE earth_date = :date")
    suspend fun getMarsPicsAtDate(date: String): AllPicsFromDate
}