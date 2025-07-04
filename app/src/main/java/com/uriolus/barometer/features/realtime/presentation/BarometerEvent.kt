package com.uriolus.barometer.features.realtime.presentation

sealed interface BarometerEvent {
    data object OnBarometerResetTendency : BarometerEvent
}
