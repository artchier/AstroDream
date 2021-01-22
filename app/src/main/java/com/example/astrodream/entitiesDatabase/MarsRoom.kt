package com.example.astrodream.entitiesDatabase

import androidx.room.*
import com.example.astrodream.domain.Camera
import com.example.astrodream.domain.MarsImage

@Entity(tableName = "mars")
data class MarsRoom (
    @PrimaryKey
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
        onDelete = ForeignKey.CASCADE
    )]
)
data class MarsPicRoom (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var url: String,
    var cameraFullName: String,
    @ColumnInfo(index = true)
    val earth_date: String
)

data class AllPicsFromDate(
    @Embedded
    val marsRoom: MarsRoom,
    @Relation(
        parentColumn = "earth_date",
        entityColumn = "earth_date"
    )
    val marsPics: List<MarsPicRoom>
)
