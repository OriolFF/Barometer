package com.uriolus.barometer.background.domain

import com.uriolus.barometer.background.data.database.entity.PressureReading
import com.uriolus.barometer.background.domain.model.BarometerReading
import kotlinx.coroutines.flow.Flow

interface PressureRepository {
    suspend fun saveReading(reading: BarometerReading)
    fun getReadingHistory(): Flow<List<PressureReading>>
}
