package com.uriolus.barometer.features.shared.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uriolus.barometer.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min
import kotlin.random.Random

/**
 * A reusable chart component that displays pressure readings over time
 */
@Composable
fun PressureChart(
    readings: List<PressureReading>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        if (readings.isEmpty()) {
            // Show empty chart with message
            Text(
                text = stringResource(id = R.string.no_enough_data),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            val textMeasurer = rememberTextMeasurer()

            // Capture colors outside of the Canvas scope
            val primaryColor = MaterialTheme.colorScheme.primary
            val onSurfaceColor = MaterialTheme.colorScheme.onSurface
            val gridLineColor = onSurfaceColor.copy(alpha = 0.2f)

            // Calculate min and max pressure values with padding
            val minPressure = remember(readings) { readings.minOf { it.pressure } - 2f }
            val maxPressure = remember(readings) { readings.maxOf { it.pressure } + 2f }
            val pressureRange = remember(minPressure, maxPressure) { maxPressure - minPressure }

            // State for zoom and pan
            var scale by remember { mutableFloatStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }

            // Define padding for the chart area
            val leftPadding = 50.dp
            val bottomPadding = 30.dp

            val minTimestamp = remember(readings) { readings.minOf { it.timestamp } }
            val maxTimestamp = remember(readings) { readings.maxOf { it.timestamp } }
            val totalTimeRange = remember(minTimestamp, maxTimestamp) { (maxTimestamp - minTimestamp).toFloat() }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.5f, 3f)

                            // Limit the panning to prevent going out of bounds
                            // Calculate max allowed offsets based on scale and chart size
                            val maxHorizontalOffset = size.width * (scale - 1f) / 2
                            val maxVerticalOffset = size.height * (scale - 1f) / 2

                            // Update offset with bounds
                            offset = Offset(
                                x = (offset.x + pan.x).coerceIn(
                                    -maxHorizontalOffset,
                                    maxHorizontalOffset
                                ),
                                y = (offset.y + pan.y).coerceIn(
                                    -maxVerticalOffset,
                                    maxVerticalOffset
                                )
                            )
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val chartWidth = size.width
                    val chartHeight = size.height

                    // Convert dp to pixels
                    val leftPaddingPx = leftPadding.toPx()
                    val bottomPaddingPx = bottomPadding.toPx()

                    // Adjusted chart dimensions
                    val adjustedChartWidth = chartWidth - leftPaddingPx
                    val adjustedChartHeight = chartHeight - bottomPaddingPx

                    // Draw Y-axis labels (pressure values)
                    val yLabelCount = 5
                    for (i in 0..yLabelCount) {
                        val pressure = minPressure + (pressureRange * i / yLabelCount)
                        val y =
                            adjustedChartHeight - (adjustedChartHeight * (pressure - minPressure) / pressureRange)

                        // Draw horizontal grid line (starting after the labels)
                        drawLine(
                            color = gridLineColor,
                            start = Offset(leftPaddingPx, y),
                            end = Offset(chartWidth, y),
                            strokeWidth = 1f
                        )

                        // Draw pressure label
                        drawText(
                            textMeasurer = textMeasurer,
                            text = String.format("%.1f", pressure),
                            topLeft = Offset(0f, y - 15.sp.toPx()),
                            style = TextStyle(color = onSurfaceColor)
                        )
                    }

                    // Draw the pressure line
                    if (readings.size > 1) {
                        val path = Path()

                        readings.forEachIndexed { index, sample ->
                            val timeOffset = sample.timestamp - minTimestamp
                            val x = leftPaddingPx + ((timeOffset / totalTimeRange) * adjustedChartWidth * scale) + offset.x
                            val normalizedY = (sample.pressure - minPressure) / pressureRange
                            val y = adjustedChartHeight - (normalizedY * adjustedChartHeight)

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
                            val timeOffset = sample.timestamp - minTimestamp
                            val x = leftPaddingPx + ((timeOffset / totalTimeRange) * adjustedChartWidth * scale) + offset.x

                            drawText(
                                textMeasurer = textMeasurer,
                                text = formatTimestampToTime(sample.timestamp),
                                topLeft = Offset(x - 20.sp.toPx(), chartHeight - 15.sp.toPx()),
                                style = TextStyle(color = onSurfaceColor)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Data class representing a pressure reading with timestamp
 */
data class PressureReading(
    val timestamp: Long,
    val pressure: Float
)

/**
 * Format a timestamp to HH:mm format
 */
private fun formatTimestampToTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}

/**
 * Preview of the PressureChart with mocked data
 */
@Preview(showBackground = true)
@Composable
fun PressureChartPreview() {
    // Generate 40 mocked pressure readings between 940 and 1030
    val mockedReadings = List(40) { index ->
        val currentTime = System.currentTimeMillis()
        // Each reading is 30 minutes apart
        val timestamp = currentTime - (40 - index) * 30 * 60 * 1000L

        // Generate random pressure between 940 and 1030
        val pressure = 940f + Random.nextFloat() * 90f

        PressureReading(timestamp, pressure)
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .height(300.dp)
        ) {
            PressureChart(readings = mockedReadings)
        }
    }
}


/**
 * Preview of the PressureChart with mocked data
 */
@Preview(showBackground = true)
@Composable
fun PressureChartPreviewNoData() {

    Surface(color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .height(300.dp)
        ) {
            PressureChart(readings = emptyList())
        }
    }
}