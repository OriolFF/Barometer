package com.uriolus.barometer.features.realtime.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uriolus.barometer.features.realtime.ui.digital.DigitalBarometerScreen
import com.uriolus.barometer.features.realtime.ui.analog.AnalogBarometerScreen

@Composable
fun BarometerScreen(data: BarometerData, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DigitalBarometerScreen(
            data = data,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        AnalogBarometerScreen(
            data = data,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
