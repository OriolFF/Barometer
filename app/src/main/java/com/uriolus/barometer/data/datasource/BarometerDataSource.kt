package com.uriolus.barometer.data.datasource

import com.uriolus.barometer.domain.model.BarometerReading
import kotlinx.coroutines.flow.Flow

interface BarometerDataSource {
    fun getBarometerReadings(): Flow<BarometerReading>
    fun start()
    fun stop()
}
