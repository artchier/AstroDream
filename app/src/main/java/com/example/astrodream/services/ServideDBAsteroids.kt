package com.example.astrodream.services

import com.example.astrodream.dao.AsteroidDAO
import com.example.astrodream.entitiesDatabase.AsteroidRoom

interface ServiceDBAsteroids {
    suspend fun addAsteroidTask(asteroide: AsteroidRoom)
    suspend fun getAllAsteroidsFavsTask(): List<AsteroidRoom>
    suspend fun deleteAsteroidsTask(asteroid: AsteroidRoom)
    suspend fun getOneAsteroidTask(name: String): AsteroidRoom?
}

class ServiceDBAsteroidsImpl(val asteroidDAO: AsteroidDAO): ServiceDBAsteroids{
    override suspend fun addAsteroidTask(asteroide: AsteroidRoom) {
        asteroidDAO.addAsteroidRoom(asteroide)
    }

    override suspend fun getAllAsteroidsFavsTask(): List<AsteroidRoom> {
        return asteroidDAO.getAllAsteroids()
    }

    override suspend fun deleteAsteroidsTask(asteroid: AsteroidRoom) {
        asteroidDAO.deleteAsteroidRoom(asteroid)
    }

    override suspend fun getOneAsteroidTask(name: String): AsteroidRoom? {
        return asteroidDAO.getAsteroid(name)
    }
}