package com.example.astrodream.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.astrodream.entitiesDatabase.Avatar

@Dao
interface AvatarDAO {

    @Insert
    suspend fun addAllAvatars(avatars: List<Avatar>)

    @Update
    suspend fun buyAvatar(avatar: Avatar)

    @Update
    suspend fun updateLastClickedAvatar(newAvatar: Avatar, oldAvatar: Avatar)

    @Query("SELECT * FROM avatar WHERE lastClicked = :lastClicked")
    suspend fun getLastClickedAvatar(lastClicked: String = "true"): Avatar

    @Query("SELECT * FROM avatar")
    suspend fun getAllAvatars(): List<Avatar>?
}