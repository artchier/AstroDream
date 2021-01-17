package com.example.astrodream.services

import com.example.astrodream.dao.TechDAO
import com.example.astrodream.entitiesDatabase.Tech

interface ServiceDatabase {
    suspend fun addTechTask(tech: Tech)

    suspend fun getAllTechnologiesTask(): List<Tech>

    suspend fun deleteAllTechnologiesTask()
}

class ServiceDatabaseImplementationTech(val techDAO: TechDAO) : ServiceDatabase {
    override suspend fun addTechTask(tech: Tech) {
        techDAO.addTech(tech)
    }

    override suspend fun getAllTechnologiesTask(): List<Tech> {
        return techDAO.getAllTechnologies()
    }

    override suspend fun deleteAllTechnologiesTask() {
        techDAO.deleteAllTechnologies()
    }
}