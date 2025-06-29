package com.uriolus.barometer.features.realtime.data.datasource

import com.uriolus.barometer.features.realtime.domain.model.BarometerReading
import kotlinx.coroutines.flow.Flow

interface BarometerDataSource {
    fun getBarometerReadings(): Flow<BarometerReading>
    fun start()
    fun stop()
}
