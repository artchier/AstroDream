package com.example.astrodream.services

import androidx.room.Query
import androidx.room.Update
import com.example.astrodream.dao.AvatarDAO
import com.example.astrodream.entitiesDatabase.Avatar

interface AvatarService {

    suspend fun addAllAvatarsTask(avatars: List<Avatar>)

    suspend fun buyAvatarTask(avatar: Avatar)

    suspend fun updateLastClickedAvatarTask(avatar1: Avatar, avatar2: Avatar)

    suspend fun getAllAvatarsTask(): List<Avatar>?
}

class AvatarServiceImplementation(private val avatarDAO: AvatarDAO) : AvatarService {
    override suspend fun addAllAvatarsTask(avatars: List<Avatar>) {
        avatarDAO.addAllAvatars(avatars)
    }

    override suspend fun buyAvatarTask(avatar: Avatar) {
        avatarDAO.buyAvatar(avatar)
    }

    override suspend fun updateLastClickedAvatarTask(avatar1: Avatar, avatar2: Avatar) {
        avatarDAO.updateLastClickedAvatar(avatar1, avatar2)
    }

    override suspend fun getAllAvatarsTask(): List<Avatar>? {
        return avatarDAO.getAllAvatars()
    }

}