package com.uriolus.barometer.background.data.datasource

import com.uriolus.barometer.background.domain.model.BarometerReading
import kotlinx.coroutines.flow.Flow

interface BarometerDataSource {
    fun getBarometerReadings(): Flow<BarometerReading>
    fun start()
    fun stop()
}
