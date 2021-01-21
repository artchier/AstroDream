package com.example.astrodream.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.astrodream.entitiesDatabase.AsteroidRoom

@Dao
interface AsteroidDAO {
    @Insert
    suspend fun addAsteroid(asteroid: AsteroidRoom)

    @Query("SELECT * FROM asteroids")
    suspend fun getAllAsteroidsFavs(): List<AsteroidRoom>

    @Delete
    suspend fun deleteAsteroid(asteroid: AsteroidRoom)
}