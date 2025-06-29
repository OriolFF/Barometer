package com.uriolus.barometer.features.realtime.data.repository

import com.uriolus.barometer.features.realtime.data.datasource.BarometerDataSource
import com.uriolus.barometer.features.realtime.domain.model.BarometerReading
import com.uriolus.barometer.features.realtime.domain.repository.BarometerRepository
import kotlinx.coroutines.flow.Flow

class BarometerRepositoryImpl(
    private val dataSource: BarometerDataSource
) : BarometerRepository {
    override fun getBarometerReadings(): Flow<BarometerReading> = dataSource.getBarometerReadings()

    override fun start() {
        dataSource.start()
    }

    override fun stop() {
        dataSource.stop()
    }
}
