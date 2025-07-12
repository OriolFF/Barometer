package com.uriolus.barometer.features.historic.domain.repository

import com.uriolus.barometer.features.historic.domain.model.PressureSample

interface HistoricRepository {
    suspend fun getAll(): List<PressureSample>
}
