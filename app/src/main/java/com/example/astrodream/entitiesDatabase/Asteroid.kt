package com.example.astrodream.entitiesDatabase

@Entity(tableName = "asteroids")
data class Asteroid(

    @PrimaryKey
    var codeAsteroid: String,

    var urlOrbit: String,

    var description: String
)