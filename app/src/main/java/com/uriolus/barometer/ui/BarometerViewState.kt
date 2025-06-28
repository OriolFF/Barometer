package com.uriolus.barometer.ui

data class BarometerViewState(
    val barometerData: BarometerData = BarometerData(1013f, 1013f),
    val isLoading: Boolean = true
)
