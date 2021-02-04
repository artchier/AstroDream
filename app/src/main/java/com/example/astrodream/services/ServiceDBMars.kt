package com.example.astrodream.services

import com.example.astrodream.dao.MarsDAO
import com.example.astrodream.entitiesDatabase.AllPicsFromDate
import com.example.astrodream.entitiesDatabase.MarsPicRoom
import com.example.astrodream.entitiesDatabase.MarsRoom

interface ServiceDB {}

interface ServiceDBMars: ServiceDB {
    suspend fun addMarsTask(mars: MarsRoom)
    suspend fun getAllMarsFavsTask(): List<MarsRoom>
    suspend fun getMarsAtDateTask(date: String): MarsRoom?
    suspend fun deleteMarsTask(mars: MarsRoom)
    suspend fun addMarsPicTask(marsPic: MarsPicRoom)
    suspend fun getMarsPicsAtDateTask(date: String): AllPicsFromDate
}

class ServiceDBImplementationMars(private val marsDAO: MarsDAO) : ServiceDBMars {
    override suspend fun addMarsTask(mars: MarsRoom) {
        marsDAO.addMars(mars)
    }

    override suspend fun getAllMarsFavsTask(): List<MarsRoom> {
        return marsDAO.getAllMarsFavs()
    }

    override suspend fun getMarsAtDateTask(date: String): MarsRoom? {
        return marsDAO.getMarsAtDate(date)
    }

    override suspend fun deleteMarsTask(mars: MarsRoom) {
        return marsDAO.deleteMars(mars)
    }

    override suspend fun addMarsPicTask(marsPic: MarsPicRoom) {
        marsDAO.addMarsPic(marsPic)
    }

    override suspend fun getMarsPicsAtDateTask(date: String): AllPicsFromDate {
        return marsDAO.getMarsPicsAtDate(date)
    }
}