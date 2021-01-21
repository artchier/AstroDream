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
    suspend fun updateLastClickedAvatar(avatar1: Avatar, avatar2: Avatar)

    @Query("SELECT * FROM avatar ORDER by price")
    suspend fun getAllAvatars(): List<Avatar>?
}