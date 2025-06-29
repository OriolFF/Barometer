package com.uriolus.barometer.features.realtime.presentation

data class BarometerViewState(
    val barometerData: BarometerData = BarometerData(1013f, 1013f),
    val isLoading: Boolean = true
)
