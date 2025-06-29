package com.uriolus.barometer.features.realtime.domain.repository

import com.uriolus.barometer.features.realtime.domain.model.BarometerReading
import kotlinx.coroutines.flow.Flow

interface BarometerRepository {
    fun getBarometerReadings(): Flow<BarometerReading>
    fun start()
    fun stop()
}
