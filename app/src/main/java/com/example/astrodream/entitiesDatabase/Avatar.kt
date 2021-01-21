package com.example.astrodream.entitiesDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "avatar")
data class Avatar(@PrimaryKey val avatarRes: Int, val price: Int, val isAvailableToBuy: String, val lastClicked: String)