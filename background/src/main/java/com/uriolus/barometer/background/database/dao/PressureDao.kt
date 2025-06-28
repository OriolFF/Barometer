package com.uriolus.barometer.background.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uriolus.barometer.background.database.entity.PressureReading
import kotlinx.coroutines.flow.Flow

@Dao
interface PressureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pressureReading: PressureReading)

    @Query("SELECT * FROM pressure_history ORDER BY timestamp DESC")
    fun getHistory(): Flow<List<PressureReading>>
}
