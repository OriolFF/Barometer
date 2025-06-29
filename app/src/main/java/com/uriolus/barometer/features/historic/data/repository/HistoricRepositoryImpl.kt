package com.uriolus.barometer.features.historic.data.repository

import com.uriolus.barometer.features.historic.data.datasource.HistoricDataSource
import com.uriolus.barometer.features.historic.domain.model.PressureSample
import com.uriolus.barometer.features.historic.domain.repository.HistoricRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoricRepositoryImpl(
    private val historicDataSource: HistoricDataSource
) : HistoricRepository {
    override fun getAll(): Flow<List<PressureSample>> {
        return historicDataSource.getAll().map { readings ->
            readings.map { entity ->
                PressureSample(
                    timestamp = entity.timestamp,
                    pressureValue = entity.pressureValue
                )
            }
        }
    }
}
