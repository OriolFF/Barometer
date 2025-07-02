package com.uriolus.barometer.features.historic.domain.usecases

import com.uriolus.barometer.features.historic.domain.model.PressureSample
import com.uriolus.barometer.features.historic.domain.repository.HistoricRepository
import kotlinx.coroutines.flow.Flow

class GetAllPressureReadingsUseCase(private val repository: HistoricRepository) {
    fun exec(): Flow<List<PressureSample>> {
        return repository.getAll()
    }
}
