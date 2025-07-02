package com.uriolus.barometer.features.realtime.presentation

import com.uriolus.barometer.features.shared.presentation.PressureReading

data class BarometerViewState(
    val barometerData: BarometerData = BarometerData(1013f, 1013f),
    val isLoading: Boolean = true,
    val pressureHistory: List<PressureReading> = emptyList()
)
