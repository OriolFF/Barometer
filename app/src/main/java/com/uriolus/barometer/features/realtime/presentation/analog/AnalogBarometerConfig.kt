package com.uriolus.barometer.features.realtime.presentation.analog

import androidx.compose.ui.graphics.Color

data class AnalogBarometerConfig(
    val millibarsRange: IntRange = 940..1060,
    val millibarsStep: Int = 10,
    val arcDegrees: Float = 300f,
    val mainColor: Color = Color(0xFFD2B48C),
    val backgroundColor: Color = Color(0xFFF5F5DC),
    val textColor: Color = Color.Black,
    val mainNeedleColor: Color = Color.Black,
    val secondNeedleColor: Color = Color(0xFFD4AF37)
)
