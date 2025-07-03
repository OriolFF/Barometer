package com.uriolus.barometer.features.historic.data.datasource

import com.uriolus.barometer.background.data.database.dao.PressureDao
import com.uriolus.barometer.background.data.database.entity.PressureReading
import kotlinx.coroutines.flow.Flow

class RoomHistoricDataSource(private val pressureDao: PressureDao) : HistoricDataSource {
    override fun getAll(): Flow<List<PressureReading>> {
        return pressureDao.getHistory()
    }
}
