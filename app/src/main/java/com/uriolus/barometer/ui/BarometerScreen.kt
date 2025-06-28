package com.uriolus.barometer.ui

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.uriolus.barometer.util.PressureConverter
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun BarometerScreen(
    data: BarometerData,
    modifier: Modifier = Modifier,
    config: BarometerConfig = BarometerConfig()
) {
    val animatedPressure by animateFloatAsState(
        targetValue = data.pressureMilliBars,
        animationSpec = tween(durationMillis = 1000),
        label = "pressureAnimation"
    )
    val animatedTendency by animateFloatAsState(
        targetValue = data.tendencyMilliBars,
        animationSpec = tween(durationMillis = 1000),
        label = "tendencyAnimation"
    )

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        BarometerDial(
            modifier = Modifier.fillMaxSize(),
            config = config
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val rimWidth = size.minDimension * 0.05f
            val dialRadius = (size.minDimension / 2f) - (rimWidth / 2f)
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val startAngle = 270 - config.arcDegrees / 2

            // Needles
            drawNeedle(animatedPressure, config.millibarsRange, startAngle, config.arcDegrees, centerX, centerY, dialRadius, config.mainNeedleColor, false, config)
            drawNeedle(animatedTendency, config.millibarsRange, startAngle, config.arcDegrees, centerX, centerY, dialRadius, config.secondNeedleColor, true, config)
        }
    }
}

@Composable
private fun BarometerDial(
    modifier: Modifier = Modifier,
    config: BarometerConfig = BarometerConfig()
) {
    Canvas(modifier = modifier) {
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
            radius = dialRadius - (rimWidth / 2f),
            center = Offset(centerX, centerY)
        )

        // Scales
        drawScale(centerX, centerY, dialRadius * 0.9f, config.millibarsRange, config.millibarsStep, startAngle, config.arcDegrees, true, config, isMillibars = true)
        drawScale(centerX, centerY, dialRadius * 0.7f, config.millibarsRange, config.millibarsStep, startAngle, config.arcDegrees, true, config, isMillibars = false)

        // Hub
        drawCircle(color = config.textColor, radius = dialRadius * 0.05f, center = Offset(centerX, centerY))

        // Labels
        val textPaint = Paint().apply {
            color = config.textColor.toArgb()
            textSize = dialRadius * 0.08f
            textAlign = Paint.Align.CENTER
        }
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawText("MILLIBARS", centerX, centerY + dialRadius * 0.9f, textPaint)
            canvas.nativeCanvas.drawText("MILLIMETRES", centerX, centerY + dialRadius * 0.7f, textPaint)
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawNeedle(
    value: Float,
    range: IntRange,
    startAngle: Float,
    sweepAngle: Float,
    centerX: Float,
    centerY: Float,
    radius: Float,
    color: Color,
    isTendencyNeedle: Boolean,
    config: BarometerConfig
) {
    val angle = valueToAngle(value, range, startAngle, sweepAngle)
    val angleRad = Math.toRadians(angle.toDouble())

    if (isTendencyNeedle) {
        val needleLength = radius * 0.7f
        val end = Offset(
            centerX + needleLength * cos(angleRad).toFloat(),
            centerY + needleLength * sin(angleRad).toFloat()
        )
        drawLine(
            color = color,
            start = Offset(centerX, centerY),
            end = end,
            strokeWidth = 2f
        )
    } else { // Main black needle
        val needleColor = Color.Black
        val needleLength = radius * 0.75f
        val tailLength = radius * 0.3f
        val arrowLength = radius * 0.1f
        val arrowWidth = radius * 0.08f

        // Shaft
        val shaftStart = Offset(
            x = centerX - tailLength * cos(angleRad).toFloat(),
            y = centerY - tailLength * sin(angleRad).toFloat()
        )
        val shaftEnd = Offset(
            x = centerX + (needleLength - arrowLength) * cos(angleRad).toFloat(),
            y = centerY + (needleLength - arrowLength) * sin(angleRad).toFloat()
        )
        drawLine(
            color = needleColor,
            start = shaftStart,
            end = shaftEnd,
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        // Arrowhead
        val tip = Offset(
            x = centerX + needleLength * cos(angleRad).toFloat(),
            y = centerY + needleLength * sin(angleRad).toFloat()
        )
        val perpRad = angleRad + Math.PI / 2
        val arrowBase1 = Offset(
            x = shaftEnd.x + (arrowWidth / 2) * cos(perpRad).toFloat(),
            y = shaftEnd.y + (arrowWidth / 2) * sin(perpRad).toFloat()
        )
        val arrowBase2 = Offset(
            x = shaftEnd.x - (arrowWidth / 2) * cos(perpRad).toFloat(),
            y = shaftEnd.y - (arrowWidth / 2) * sin(perpRad).toFloat()
        )
        val arrowPath = Path().apply {
            moveTo(tip.x, tip.y)
            lineTo(arrowBase1.x, arrowBase1.y)
            lineTo(arrowBase2.x, arrowBase2.y)
            close()
        }
        drawPath(path = arrowPath, color = needleColor)

        // Half-moon tail
        val tailRadius = radius * 0.05f
        val tailAngle = Math.toDegrees(angleRad).toFloat() + 180
        val tailRect = Rect(
            center = shaftStart,
            radius = tailRadius
        )
        drawArc(
            color = needleColor,
            startAngle = tailAngle + 90,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = tailRect.topLeft,
            size = tailRect.size
        )
    }
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
            val startCmHg = PressureConverter.mbarToCmHg(range.first)
            val endCmHg = PressureConverter.mbarToCmHg(range.last)

            val firstSubmarkTenths = ceil(startCmHg * 10).toInt()
            val lastSubmarkTenths = floor(endCmHg * 10).toInt()

            for (tenth in firstSubmarkTenths..lastSubmarkTenths) {
                if (tenth % 10 == 0) continue // Skip major ticks

                val valueCmHg = tenth / 10f
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

private fun valueToAngle(value: Float, range: IntRange, startAngle: Float, sweepAngle: Float): Float {
    val rangeSize = (range.last - range.first).toFloat()
    if (rangeSize == 0f) return startAngle
    val valueRatio = (value - range.first) / rangeSize
    return startAngle + valueRatio * sweepAngle
}

@Preview(showBackground = true)
@Composable
fun BarometerScreenPreview() {
    BarometerScreen(BarometerData(1010f, 1030f))
}
