package com.example.astrodream.entitiesDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.astrodream.domain.Camera
import com.example.astrodream.domain.MarsImage

@Entity(tableName = "mars")
data class MarsRoom (

    @PrimaryKey
    var earth_date: String,
    var sol: Long,
//    var img_list: List<MarsImage>,
    var maxTemp: String,
    var minTemp: String

)