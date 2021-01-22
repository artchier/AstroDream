package com.example.astrodream.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.astrodream.dao.AvatarDAO
import com.example.astrodream.dao.DailyDAO
import com.example.astrodream.dao.MarsDAO
import com.example.astrodream.dao.AsteroidDAO
import com.example.astrodream.dao.TechDAO
import com.example.astrodream.entitiesDatabase.*

@Database(
    entities = [Tech::class,
        MarsRoom::class,
        MarsPicRoom::class,
        DailyRoom::class,
        Avatar::class,
        AsteroidRoom::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun techDAO(): TechDAO
    abstract fun marsDAO(): MarsDAO
    abstract fun dailyDAO(): DailyDAO
    abstract fun avatarDAO(): AvatarDAO
    abstract fun asteroidDAO(): AsteroidDAO

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