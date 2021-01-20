package com.example.astrodream.services

import com.example.astrodream.dao.TechDAO
import com.example.astrodream.entitiesDatabase.Tech

interface ServiceDatabaseTech {
    suspend fun addTechTask(tech: Tech)

    suspend fun deleteTechTask(codReference: String)

    suspend fun getAllTechnologiesTask(): List<Tech>

    suspend fun getTechByCodeTask(codReference: String): Tech

    suspend fun deleteAllTechnologiesTask()
}

class ServiceDatabaseImplementationTech(val techDAO: TechDAO) : ServiceDatabaseTech {
    override suspend fun addTechTask(tech: Tech) {
        techDAO.addTech(tech)
    }

    override suspend fun deleteTechTask(codReference: String) {
        techDAO.deleteTech(codReference)
    }

    override suspend fun getAllTechnologiesTask(): List<Tech> {
        return techDAO.getAllTechnologies()
    }

    override suspend fun getTechByCodeTask(codReference: String): Tech {
        return techDAO.getTechByCode(codReference)
    }

    override suspend fun deleteAllTechnologiesTask() {
        techDAO.deleteAllTechnologies()
    }
}