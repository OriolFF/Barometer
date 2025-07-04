package com.uriolus.barometer.features.historic.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uriolus.barometer.features.historic.domain.model.PressureSample
import com.uriolus.barometer.features.realtime.presentation.theme.BarometerTheme
import com.uriolus.barometer.features.shared.presentation.PressureChart
import com.uriolus.barometer.features.shared.presentation.PressureReading
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

@Composable
fun HistoricScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoricViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    Box(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column {
                // Chart section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(240.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    val chartReadings = remember(state.readings) {
                        val last100Readings = state.readings.takeLast(100)
                        last100Readings.map { sample ->
                            PressureReading(sample.timestamp, sample.pressureValue)
                        }
                    }
                    if (chartReadings.isNotEmpty()) {
                        PressureChart(
                            readings = chartReadings,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                // List section
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.readings) { reading ->
                        HistoricReadingItem(reading = reading)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun HistoricReadingItem(reading: PressureSample, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${reading.pressureValue} hPa",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = formatTimestamp(reading.timestamp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}

@Preview(showBackground = true)
@Composable
fun HistoricScreenPreview() {
    BarometerTheme {
        val viewModel: HistoricViewModel = koinViewModel()
        HistoricScreen(viewModel = viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun HistoricReadingItemPreview() {
    BarometerTheme {
        HistoricReadingItem(
            reading = PressureSample(
                timestamp = System.currentTimeMillis(),
                pressureValue = 1015.2f
            )
        )
    }
}
