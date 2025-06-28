package com.uriolus.barometer.background.datasource

import com.uriolus.barometer.background.model.BarometerReading
import kotlinx.coroutines.flow.Flow

interface BarometerDataSource {
    fun getBarometerReadings(): Flow<BarometerReading>
    fun start()
    fun stop()
}
