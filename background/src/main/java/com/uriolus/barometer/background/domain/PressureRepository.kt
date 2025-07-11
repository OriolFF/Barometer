package com.uriolus.barometer.background.domain

import com.uriolus.barometer.background.data.database.entity.PressureReading
import com.uriolus.barometer.background.domain.model.BarometerReading

interface PressureRepository {
    suspend fun saveReading(reading: BarometerReading)
    suspend fun getReadingHistory(): List<PressureReading>
}
