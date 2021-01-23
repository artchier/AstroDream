package com.example.astrodream.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.astrodream.entitiesDatabase.DailyRoom

@Dao
interface DailyDAO {

    @Insert
    suspend fun addDaily(daily: DailyRoom)

    @Query("SELECT * FROM daily")
    suspend fun getAllDailyFavs(): List<DailyRoom>

    @Query("SELECT * FROM daily WHERE date = :date")
    suspend fun getDailyAtDate(date: String): DailyRoom?

    @Delete
    suspend fun deleteDaily(daily: DailyRoom)
    
}