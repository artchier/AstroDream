package com.example.astrodream.services

import com.example.astrodream.dao.MarsDAO
import com.example.astrodream.domain.PlainClass
import com.example.astrodream.entitiesDatabase.MarsRoom

interface ServiceDBMars {
    suspend fun addMarsTask(mars: MarsRoom)
    suspend fun getAllMarsFavsTask(): List<MarsRoom>
    suspend fun getMarsAtDateTask(date: String): MarsRoom
    suspend fun deleteMarsTask(mars: MarsRoom)
}

class ServiceDBImplementationMars(val marsDAO: MarsDAO) : ServiceDBMars {
    override suspend fun addMarsTask(mars: MarsRoom) {
        marsDAO.addMars(mars)
    }

    override suspend fun getAllMarsFavsTask(): List<MarsRoom> {
        return marsDAO.getAllMarsFavs()
    }

    override suspend fun getMarsAtDateTask(date: String): MarsRoom {
        return marsDAO.getMarsAtDate(date)
    }

    override suspend fun deleteMarsTask(mars: MarsRoom) {
        return marsDAO.deleteMars(mars)
    }
}