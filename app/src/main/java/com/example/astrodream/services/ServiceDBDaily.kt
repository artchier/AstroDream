package com.example.astrodream.services

import com.example.astrodream.dao.DailyDAO
import com.example.astrodream.entitiesDatabase.DailyRoom

interface ServiceDBDaily: ServiceDB {
    suspend fun addDailyTask(daily: DailyRoom)
    suspend fun getAllDailyFavsTask(): List<DailyRoom>
    suspend fun getDailyAtDateTask(date: String): DailyRoom
    suspend fun deleteDailyTask(daily: DailyRoom)
}

class ServiceDBImplementationDaily(private val dailyDAO: DailyDAO) : ServiceDBDaily {
    override suspend fun addDailyTask(daily: DailyRoom) {
        dailyDAO.addDaily(daily)
    }

    override suspend fun getAllDailyFavsTask(): List<DailyRoom> {
        return dailyDAO.getAllDailyFavs()
    }

    override suspend fun getDailyAtDateTask(date: String): DailyRoom {
        return dailyDAO.getDailyAtDate(date)
    }

    override suspend fun deleteDailyTask(daily: DailyRoom) {
        return dailyDAO.deleteDaily(daily)
    }
}