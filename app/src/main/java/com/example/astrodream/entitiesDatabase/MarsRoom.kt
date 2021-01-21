package com.example.astrodream.entitiesDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.astrodream.domain.Camera
import com.example.astrodream.domain.MarsImage

@Entity(tableName = "mars")
data class MarsRoom (

    @PrimaryKey
    @ColumnInfo(name = "earth_date")
    var earth_date: String,
    var sol: Long,
    var maxTemp: String,
    var minTemp: String,
//    var img_list: List<MarsImage>,

)

@Entity(
    foreignKeys = [ForeignKey(
        entity = MarsRoom::class,
        parentColumns = ["earth_date"],
        childColumns = ["earth_date"],
        onDelete = ForeignKey.NO_ACTION
    )]
)
data class MarsImgsRoom (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "earth_date")
    var earth_date: String,

    var url: String,

)

