package com.example.astrodream.services

import com.example.astrodream.dao.AvatarDAO
import com.example.astrodream.entitiesDatabase.Avatar

interface AvatarService {

    suspend fun addAllAvatarsTask(avatars: List<Avatar>)

    suspend fun buyAvatarTask(avatar: Avatar)

    suspend fun updateLastClickedAvatarTask(newAvatar: Avatar, oldAvatar: Avatar)

    suspend fun getLastClickedAvatarTask(): Avatar

    suspend fun getAllAvatarsTask(): List<Avatar>?
}

class AvatarServiceImplementation(private val avatarDAO: AvatarDAO) : AvatarService {
    override suspend fun addAllAvatarsTask(avatars: List<Avatar>) {
        avatarDAO.addAllAvatars(avatars)
    }

    override suspend fun buyAvatarTask(avatar: Avatar) {
        avatarDAO.buyAvatar(avatar)
    }

    override suspend fun updateLastClickedAvatarTask(newAvatar: Avatar, oldAvatar: Avatar) {
        avatarDAO.updateLastClickedAvatar(newAvatar, oldAvatar)
    }

    override suspend fun getLastClickedAvatarTask(): Avatar {
        return avatarDAO.getLastClickedAvatar()
    }

    override suspend fun getAllAvatarsTask(): List<Avatar>? {
        return avatarDAO.getAllAvatars()
    }

}