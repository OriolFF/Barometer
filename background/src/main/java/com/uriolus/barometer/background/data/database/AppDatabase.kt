package com.uriolus.barometer.background.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uriolus.barometer.background.data.database.dao.PressureDao
import com.uriolus.barometer.background.data.database.entity.PressureReading

@Database(entities = [PressureReading::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pressureDao(): PressureDao
}
