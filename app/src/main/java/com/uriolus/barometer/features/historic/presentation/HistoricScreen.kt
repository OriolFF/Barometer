package com.uriolus.barometer.features.historic.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uriolus.barometer.features.historic.domain.model.PressureSample
import com.uriolus.barometer.features.realtime.presentation.theme.BarometerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min
import kotlin.text.*

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
                // The chart expects data from oldest to newest for a correct timeline
                val ascendingReadings = remember(state.readings) { state.readings.reversed() }
                
                PressureChart(
                    readings = ascendingReadings,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(16.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(8.dp)
                )
            }
            
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // The list shows newest readings first, so we use the original list
                items(state.readings) { reading ->
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
    if (readings.isEmpty()) return
    
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    
    // Capture colors outside of the Canvas scope
    val primaryColor = MaterialTheme.colorScheme.primary
    val gridLineColor = Color.Gray.copy(alpha = 0.3f)
    
    // Calculate min and max pressure values with padding
    val minPressure = remember(readings) { readings.minOf { it.pressureValue } - 2f }
    val maxPressure = remember(readings) { readings.maxOf { it.pressureValue } + 2f }
    val pressureRange = remember(minPressure, maxPressure) { maxPressure - minPressure }
    
    // State for zoom and pan
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    
    Box(modifier = modifier
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                scale = (scale * zoom).coerceIn(0.5f, 3f)
                offset += pan
            }
        }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val chartWidth = size.width
            val chartHeight = size.height
            
            // Draw Y-axis labels (pressure values)
            val yLabelCount = 5
            for (i in 0..yLabelCount) {
                val pressure = minPressure + (pressureRange * i / yLabelCount)
                val y = chartHeight - (chartHeight * (pressure - minPressure) / pressureRange)
                
                // Draw horizontal grid line
                drawLine(
                    color = gridLineColor,
                    start = Offset(0f, y),
                    end = Offset(chartWidth, y),
                    strokeWidth = 1f
                )
                
                // Draw pressure label
                drawText(
                    textMeasurer = textMeasurer,
                    text = String.format("%.1f", pressure),
                    topLeft = Offset(0f, y - 15.sp.toPx())
                )
            }
            
            // Draw the pressure line
            if (readings.size > 1) {
                val path = Path()
                val pointWidth = chartWidth / (readings.size - 1)
                
                readings.forEachIndexed { index, sample ->
                    val x = index * pointWidth * scale + offset.x
                    val normalizedY = (sample.pressureValue - minPressure) / pressureRange
                    val y = chartHeight - (normalizedY * chartHeight)
                    
                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                    
                    // Draw point
                    drawCircle(
                        color = primaryColor,
                        radius = 3.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
                
                // Draw the line connecting all points
                drawPath(
                    path = path,
                    color = primaryColor,
                    style = Stroke(width = 2.dp.toPx())
                )
                
                // Draw X-axis time labels (only a few to avoid overcrowding)
                val labelCount = min(readings.size, 5)
                for (i in 0 until labelCount) {
                    val index = (readings.size - 1) * i / (labelCount - 1)
                    val sample = readings[index]
                    val x = index * pointWidth * scale + offset.x
                    
                    drawText(
                        textMeasurer = textMeasurer,
                        text = formatTimestampToTime(sample.timestamp),
                        topLeft = Offset(x - 20.sp.toPx(), chartHeight - 15.sp.toPx())
                    )
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
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
