package com.example.astrodream.entitiesDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroids")
data class AsteroidRoom(

    @PrimaryKey
    var codeAsteroid: String,

    var urlOrbit: String,

    var description: String
)