package com.uriolus.barometer.features.historic.presentation

import com.uriolus.barometer.features.historic.domain.model.PressureSample

data class HistoricState(
    val readings: List<PressureSample> = emptyList(),
    val isLoading: Boolean = true
)
