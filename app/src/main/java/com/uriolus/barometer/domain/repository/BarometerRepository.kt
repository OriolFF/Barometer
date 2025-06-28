package com.uriolus.barometer.domain.repository

import com.uriolus.barometer.domain.model.BarometerReading
import kotlinx.coroutines.flow.Flow

interface BarometerRepository {
    fun getBarometerReadings(): Flow<BarometerReading>
    fun start()
    fun stop()
}
