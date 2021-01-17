package com.example.astrodream.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.astrodream.entitiesDatabase.Tech

@Dao
interface TechDAO {
    @Insert
    suspend fun addTech(tech: Tech)

    @Query("SELECT * FROM technologies")
    suspend fun getAllTechnologies(): List<Tech>

    @Query("DELETE FROM technologies")
    suspend fun deleteAllTechnologies()
}