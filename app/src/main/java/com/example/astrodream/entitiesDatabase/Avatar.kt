package com.example.astrodream.entitiesDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "avatar")
data class Avatar(
    val avatarRes: Int,
    val price: Int,
    var isAvailableToBuy: String,
    var lastClicked: String
) {
    @PrimaryKey(autoGenerate = true)
    var idAvatar: Int = 0

    override fun toString(): String {
        return "idAvatar: $idAvatar\navatarRes: $avatarRes\nprice: $price\nisAvailableToBuy: $isAvailableToBuy\nlastClicked: $lastClicked"
    }
}