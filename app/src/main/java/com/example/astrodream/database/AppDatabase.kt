package com.example.astrodream.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.astrodream.dao.DailyDAO
import com.example.astrodream.dao.MarsDAO
import com.example.astrodream.dao.TechDAO
import com.example.astrodream.entitiesDatabase.DailyRoom
import com.example.astrodream.entitiesDatabase.MarsRoom
import com.example.astrodream.entitiesDatabase.Tech

@Database(entities = [Tech::class, MarsRoom::class, DailyRoom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun techDAO(): TechDAO
    abstract fun marsDAO(): MarsDAO
    abstract fun dailyDAO(): DailyDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database.db"
        ).build()
    }
}