package com.example.astrodream.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.astrodream.entitiesDatabase.AsteroidRoom

@Dao
interface AsteroidDAO {
    @Insert
    suspend fun addAsteroidRoom(asteroid: AsteroidRoom)

    @Query("SELECT * FROM asteroids")
    suspend fun getAllAsteroids(): List<AsteroidRoom>

    @Delete
    suspend fun deleteAsteroidRoom(asteroid: AsteroidRoom)

    @Query("SELECT * FROM asteroids where codeAsteroid LIKE :asteroidName")
    fun getAsteroid(asteroidName: String): AsteroidRoom?
}