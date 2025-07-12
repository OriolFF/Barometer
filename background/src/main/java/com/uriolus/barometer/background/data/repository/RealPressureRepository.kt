package com.uriolus.barometer.background.data.repository

import com.uriolus.barometer.background.data.database.dao.PressureDao
import com.uriolus.barometer.background.data.database.entity.PressureReading
import com.uriolus.barometer.background.domain.PressureRepository
import com.uriolus.barometer.background.domain.model.BarometerReading

class RealPressureRepository(
    private val pressureDao: PressureDao
) : PressureRepository {
    
    override suspend fun saveReading(reading: BarometerReading) {
        val pressureEntity = PressureReading(
            timestamp = reading.timestamp,
            pressureValue = reading.pressure
        )
        pressureDao.insert(pressureEntity)
    }
    
    override suspend fun getReadingHistory(): List<PressureReading> {
        return pressureDao.getHistory()
    }
}
