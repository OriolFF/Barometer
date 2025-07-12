package com.uriolus.barometer.features.historic.domain.usecases

import com.uriolus.barometer.features.historic.domain.model.PressureSample
import com.uriolus.barometer.features.historic.domain.repository.HistoricRepository

class GetAllPressureReadingsUseCase(private val repository: HistoricRepository) {
    suspend fun exec(): List<PressureSample> {
        return repository.getAll()
    }
}
