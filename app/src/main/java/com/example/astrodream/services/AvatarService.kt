package com.example.astrodream.services

import com.example.astrodream.dao.AvatarDAO
import com.example.astrodream.entitiesDatabase.AvatarRoom

interface AvatarService {

    suspend fun addAllAvatarsTask(avatars: List<AvatarRoom>)

    suspend fun getAllAvatarsTask(): List<AvatarRoom>?
}

class AvatarServiceImplementation(private val avatarDAO: AvatarDAO) : AvatarService {
    override suspend fun addAllAvatarsTask(avatars: List<AvatarRoom>) {
        avatarDAO.addAllAvatars(avatars)
    }

    override suspend fun getAllAvatarsTask(): List<AvatarRoom>? {
        return avatarDAO.getAllAvatars()
    }

}