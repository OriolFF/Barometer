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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import com.uriolus.barometer.features.historic.domain.model.PressureSample
import com.uriolus.barometer.features.realtime.presentation.theme.BarometerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoricScreen(
    state: HistoricState,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Reverse the list once to show oldest first in chart
        val ascendingReadings = remember(state.readings) { state.readings.reversed() }

        Column(modifier = modifier.fillMaxSize()) {
            if (ascendingReadings.isNotEmpty()) {
                PressureChart(
                    readings = ascendingReadings,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(16.dp)
                )
            }
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.readings) { reading -> // Use original list for newest first
                    HistoricReadingItem(reading = reading)
                }
            }
        }
    }
}

@Composable
fun HistoricReadingItem(reading: PressureSample, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
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
}

@Composable
fun PressureChart(readings: List<PressureSample>, modifier: Modifier = Modifier) {
    val modelProducer = remember { ChartEntryModelProducer() }
    val dataset = remember(readings) {
        readings.mapIndexed { index, pressureSample ->
            entryOf(index.toFloat(), pressureSample.pressureValue)
        }
    }

    LaunchedEffect(dataset) {
        modelProducer.setEntries(dataset)
    }

    // This check ensures that the chart data is loaded in preview mode,
    // as LaunchedEffect does not run in previews.
    if (LocalInspectionMode.current) {
        modelProducer.setEntries(dataset)
    }

    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            readings.getOrNull(value.toInt())
                ?.let { formatTimestampToTime(it.timestamp) }
                .orEmpty()
        }

    Chart(
        modifier = modifier,
        chart = lineChart(),
        chartModelProducer = modelProducer,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(
            valueFormatter = bottomAxisValueFormatter,
            guideline = null,
            tickLength = 0.dp
        ),
    )
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}

private fun formatTimestampToTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}

@Preview(showBackground = true)
@Composable
fun HistoricScreenPreview() {
    BarometerTheme {
        val sampleReadings = remember {
            listOf(
                PressureSample(timestamp = System.currentTimeMillis() - 30000, pressureValue = 1012.8f),
                PressureSample(timestamp = System.currentTimeMillis() - 20000, pressureValue = 1013.0f),
                PressureSample(timestamp = System.currentTimeMillis() - 10000, pressureValue = 1012.5f),
                PressureSample(timestamp = System.currentTimeMillis(), pressureValue = 1014.1f)
            )
        }
        val state = HistoricState(readings = sampleReadings.reversed(), isLoading = false)
        HistoricScreen(state = state)
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
