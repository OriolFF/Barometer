package com.uriolus.barometer.features.historic.domain.repository

import com.uriolus.barometer.features.historic.domain.model.PressureSample
import kotlinx.coroutines.flow.Flow

interface HistoricRepository {
    fun getAll(): Flow<List<PressureSample>>
}
