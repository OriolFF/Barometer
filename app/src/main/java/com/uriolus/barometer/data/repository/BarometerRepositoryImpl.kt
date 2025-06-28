package com.uriolus.barometer.data.repository

import com.uriolus.barometer.data.datasource.BarometerDataSource
import com.uriolus.barometer.domain.model.BarometerReading
import com.uriolus.barometer.domain.repository.BarometerRepository
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
