package com.uriolus.barometer.features.historic.data.datasource

import com.uriolus.barometer.background.data.database.entity.PressureReading
import kotlinx.coroutines.flow.Flow

interface HistoricDataSource {
    fun getAll(): Flow<List<PressureReading>>
}
