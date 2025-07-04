package com.uriolus.barometer.features.historic.data.repository

import com.uriolus.barometer.features.historic.data.datasource.HistoricDataSource
import com.uriolus.barometer.features.historic.domain.model.PressureSample
import com.uriolus.barometer.features.historic.domain.repository.HistoricRepository


class HistoricRepositoryImpl(
    private val historicDataSource: HistoricDataSource
) : HistoricRepository {
    override suspend fun getAll(): List<PressureSample> {
        return historicDataSource.getAll()
            .map { reading ->
                PressureSample(
                    timestamp = reading.timestamp,
                    pressureValue = reading.pressureValue
                )
            }
    }
}
