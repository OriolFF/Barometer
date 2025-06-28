package com.uriolus.barometer.ui

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.uriolus.barometer.util.PressureConverter
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun BarometerScreen(
    data: BarometerData,
    modifier: Modifier = Modifier,
    config: BarometerConfig = BarometerConfig()
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
        ) {
            val rimWidth = size.minDimension * 0.05f
            val dialRadius = (size.minDimension / 2f) - (rimWidth / 2f)
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val startAngle = 270 - config.arcDegrees / 2

            // Rim
            val rimBrush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFFEADCA6), // Highlight
                    config.mainColor,
                    Color(0xFF8C7B60), // Shadow
                    config.mainColor,
                    Color(0xFFEADCA6)  // Highlight
                ),
                center = Offset(centerX, centerY)
            )
            drawCircle(
                brush = rimBrush,
                radius = dialRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = rimWidth)
            )

            // Background
            drawCircle(
                color = config.backgroundColor,
                radius = dialRadius - (dialRadius * 0.05f),
                center = Offset(centerX, centerY)
            )

            // Millibars Scale (Outer)
            drawScale(centerX, centerY, dialRadius * 0.9f, config.millibarsRange, config.millibarsStep, startAngle, config.arcDegrees, true, config, isMillibars = true)
            // Millimeters Scale (Inner)
            drawScale(centerX, centerY, dialRadius * 0.7f, config.millibarsRange, config.millibarsStep, startAngle, config.arcDegrees, true, config, isMillibars = false)

            // Needles
            drawNeedle(data.pressureMilliBars, config.millibarsRange, startAngle, config.arcDegrees, centerX, centerY, dialRadius * 0.8f, config.mainNeedleColor, 8f)
            drawNeedle(data.tendencyMilliBars, config.millibarsRange, startAngle, config.arcDegrees, centerX, centerY, dialRadius * 0.75f, config.secondNeedleColor, 4f)

            // Hub
            drawCircle(color = config.textColor, radius = dialRadius * 0.05f, center = Offset(centerX, centerY))

            // Unit Text
            drawIntoCanvas { canvas ->
                val paint = Paint().apply {
                    color = config.textColor.toArgb()
                    textSize = dialRadius * 0.08f
                    textAlign = Paint.Align.CENTER
                }
                canvas.nativeCanvas.drawText("MILLIBARS", centerX, centerY + dialRadius * 0.9f, paint)
                canvas.nativeCanvas.drawText("MILLIMETRES", centerX, centerY + dialRadius * 0.7f, paint)
            }
        }
    }
}

private fun valueToAngle(value: Float, range: IntRange, startAngle: Float, sweepAngle: Float): Float {
    val rangeSize = (range.last - range.first).toFloat()
    if (rangeSize == 0f) return startAngle
    val valueRatio = (value - range.first) / rangeSize
    return startAngle + valueRatio * sweepAngle
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawNeedle(
    value: Float,
    range: IntRange,
    startAngle: Float,
    sweepAngle: Float,
    centerX: Float,
    centerY: Float,
    length: Float,
    color: androidx.compose.ui.graphics.Color,
    strokeWidth: Float
) {
    val angle = valueToAngle(value, range, startAngle, sweepAngle)
    val angleRad = Math.toRadians(angle.toDouble())
    val endX = centerX + length * cos(angleRad).toFloat()
    val endY = centerY + length * sin(angleRad).toFloat()
    drawLine(color, Offset(centerX, centerY), Offset(endX, endY), strokeWidth = strokeWidth)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawScale(
    centerX: Float, centerY: Float, radius: Float, range: IntRange, step: Int, startAngle: Float, sweepAngle: Float, drawSubMarks: Boolean, config: BarometerConfig, isMillibars: Boolean
) {
    val textPaint = Paint().apply {
        color = config.textColor.toArgb()
        textSize = radius * 0.1f
        textAlign = Paint.Align.CENTER
    }

    // Major ticks and labels
    if (isMillibars) {
        for (value in range.first..range.last step step) {
            val angle = valueToAngle(value.toFloat(), range, startAngle, sweepAngle)
            val angleRad = Math.toRadians(angle.toDouble())
            val startRadius = radius
            val endRadius = radius - (if (drawSubMarks) 20f else 15f)
            val start = Offset(centerX + startRadius * cos(angleRad).toFloat(), centerY + startRadius * sin(angleRad).toFloat())
            val end = Offset(centerX + endRadius * cos(angleRad).toFloat(), centerY + endRadius * sin(angleRad).toFloat())
            drawLine(config.textColor, start, end, strokeWidth = 3f)

            val textRadius = radius - 60f
            val textX = centerX + textRadius * cos(angleRad).toFloat()
            val textY = centerY + textRadius * sin(angleRad).toFloat() + textPaint.textSize / 3
            val label = value.toString()
            drawIntoCanvas { it.nativeCanvas.drawText(label, textX, textY, textPaint) }
        }
    } else {
        val minCmHg = PressureConverter.mbarToCmHg(range.first).roundToInt()
        val maxCmHg = PressureConverter.mbarToCmHg(range.last).roundToInt()
        for (cmHgValue in minCmHg..maxCmHg) {
            val correspondingMbar = PressureConverter.cmHgToMbar(cmHgValue)
            if (correspondingMbar < range.first || correspondingMbar > range.last) continue

            val angle = valueToAngle(correspondingMbar, range, startAngle, sweepAngle)
            val angleRad = Math.toRadians(angle.toDouble())
            val startRadius = radius
            val endRadius = radius - 20f
            val start = Offset(centerX + startRadius * cos(angleRad).toFloat(), centerY + startRadius * sin(angleRad).toFloat())
            val end = Offset(centerX + endRadius * cos(angleRad).toFloat(), centerY + endRadius * sin(angleRad).toFloat())
            drawLine(config.textColor, start, end, strokeWidth = 3f)

            val textRadius = radius - 60f
            val textX = centerX + textRadius * cos(angleRad).toFloat()
            val textY = centerY + textRadius * sin(angleRad).toFloat() + textPaint.textSize / 3
            val label = cmHgValue.toString()
            drawIntoCanvas { it.nativeCanvas.drawText(label, textX, textY, textPaint) }
        }
    }

    // Sub-marks
    if (drawSubMarks) {
        if (isMillibars) { // Millibars logic
            for (value in range.first..range.last) {
                if (value % step != 0) {
                    val angle = valueToAngle(value.toFloat(), range, startAngle, sweepAngle)
                    val angleRad = Math.toRadians(angle.toDouble())
                    val startRadius = radius
                    val endRadius = radius - 10f
                    val start = Offset(centerX + startRadius * cos(angleRad).toFloat(), centerY + startRadius * sin(angleRad).toFloat())
                    val end = Offset(centerX + endRadius * cos(angleRad).toFloat(), centerY + endRadius * sin(angleRad).toFloat())
                    drawLine(config.textColor, start, end, strokeWidth = 1f)
                }
            }
        } else { // Millimeters logic
            val minCmHg = PressureConverter.mbarToCmHg(range.first).roundToInt()
            val maxCmHg = PressureConverter.mbarToCmHg(range.last).roundToInt()
            val subMarkCount = 10
            for (majorValue in minCmHg until maxCmHg) {
                for (i in 1 until subMarkCount) {
                    val valueCmHg = majorValue + i.toFloat() / subMarkCount
                    val correspondingMbar = PressureConverter.cmHgToMbar(valueCmHg)
                    if (correspondingMbar < range.first || correspondingMbar > range.last) continue

                    val angle = valueToAngle(correspondingMbar, range, startAngle, sweepAngle)
                    val angleRad = Math.toRadians(angle.toDouble())
                    val startRadius = radius
                    val endRadius = radius - 10f
                    val start = Offset(centerX + startRadius * cos(angleRad).toFloat(), centerY + startRadius * sin(angleRad).toFloat())
                    val end = Offset(centerX + endRadius * cos(angleRad).toFloat(), centerY + endRadius * sin(angleRad).toFloat())
                    drawLine(config.textColor, start, end, strokeWidth = 1f)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarometerScreenPreview() {
    BarometerScreen(BarometerData(980f, 1030f))
}
