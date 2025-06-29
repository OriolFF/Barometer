package com.uriolus.barometer.features.historic.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.entryModelOf
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
        Column(modifier = modifier.fillMaxSize()) {
            if (state.readings.isNotEmpty()) {
                PressureChart(
                    readings = state.readings.reversed(), // Reverse to show oldest first
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(16.dp)
                )
            }
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(state.readings) { reading ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(text = "Pressure: ${reading.pressureValue} hPa")
                        Text(
                            text = "Time: ${formatTimestamp(reading.timestamp)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PressureChart(readings: List<PressureSample>, modifier: Modifier = Modifier) {
    val chartEntryModel = entryModelOf(*readings.mapIndexed { index, pressureSample ->
        index to pressureSample.pressureValue
    }.toTypedArray())

    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            formatTimestampToTime(readings[value.toInt()].timestamp)
        }

    ProvideChartStyle {
        Chart(
            modifier = modifier,
            chart = lineChart(),
            model = chartEntryModel,
            startAxis = startAxis(),
            bottomAxis = bottomAxis(
                valueFormatter = bottomAxisValueFormatter,
                guideline = null,
                tickLength = 0.dp
            ),
        )
    }
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
        val sampleReadings = listOf(
            PressureSample(timestamp = System.currentTimeMillis() - 30000, pressureValue = 1012.8f),
            PressureSample(timestamp = System.currentTimeMillis() - 20000, pressureValue = 1013.0f),
            PressureSample(timestamp = System.currentTimeMillis() - 10000, pressureValue = 1012.5f),
            PressureSample(timestamp = System.currentTimeMillis(), pressureValue = 1014.1f)
        )
        val state = HistoricState(readings = sampleReadings.reversed(), isLoading = false)
        HistoricScreen(state = state)
    }
}
