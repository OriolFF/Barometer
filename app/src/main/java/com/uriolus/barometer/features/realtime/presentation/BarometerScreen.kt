package com.uriolus.barometer.features.realtime.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uriolus.barometer.features.realtime.presentation.digital.DigitalBarometerScreen
import com.uriolus.barometer.features.realtime.presentation.analog.AnalogBarometerScreen
import com.uriolus.barometer.features.shared.presentation.PressureChart
import com.uriolus.barometer.features.shared.presentation.PressureReading

@Composable
fun BarometerScreen(data: BarometerData, modifier: Modifier = Modifier) {
    // Keep a history of pressure readings for the chart
    val pressureHistory = remember { mutableStateListOf<PressureReading>() }
    
    // Add current reading to history
    LaunchedEffect(data.pressureMilliBars) {
        // Add new reading to history
        val newReading = PressureReading(
            timestamp = System.currentTimeMillis(),
            pressure = data.pressureMilliBars
        )
        
        // Add to history, keeping only the last 20 readings
        pressureHistory.add(newReading)
        if (pressureHistory.size > 20) {
            pressureHistory.removeAt(0)
        }
    }
    
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
        Spacer(modifier = Modifier.height(24.dp))
        
        // Pressure chart section
        if (pressureHistory.size > 1) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Pressure Trend",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    PressureChart(
                        readings = pressureHistory,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
