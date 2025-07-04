package com.uriolus.barometer.features.historic.data.datasource

import com.uriolus.barometer.background.data.database.dao.PressureDao
import com.uriolus.barometer.background.data.database.entity.PressureReading

class RoomHistoricDataSource(private val pressureDao: PressureDao) : HistoricDataSource {
    override suspend fun getAll(): List<PressureReading> {
        return pressureDao.getHistory()
    }
}
