package com.example.astrodream.entitiesDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "technologies")
data class Tech(

    @PrimaryKey
    var codReferenceTech: String,

    var titleTech: String,

    var descTech: String
)

