package com.uriolus.barometer.background.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uriolus.barometer.background.data.database.entity.PressureReading

@Dao
interface PressureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pressureReading: PressureReading)

    @Query("SELECT * FROM pressure_history ORDER BY timestamp DESC")
    suspend fun getHistory(): List<PressureReading>
}
