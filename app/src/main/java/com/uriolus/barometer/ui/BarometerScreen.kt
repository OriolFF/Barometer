package com.uriolus.barometer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BarometerScreen(data: BarometerData) {
    Canvas(modifier = Modifier
        .fillMaxSize()
        .aspectRatio(1f)) {
        val dialRadius = size.minDimension / 2
        val centerX = size.width / 2
        val centerY = size.height / 2

        // Draw dial
        drawCircle(
            color = Color(0xFFD2B48C),
            radius = dialRadius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 20f)
        )

        // Draw background
        drawCircle(
            color = Color(0xFFF5F5DC),
            radius = dialRadius - 10,
            center = Offset(centerX, centerY)
        )

        // Draw scale
        for (i in 960..1050 step 10) {
            val angle = (i - 960) * (270f / 90f) - 135
            val angleRad = Math.toRadians(angle.toDouble())
            val startRadius = dialRadius - 20
            val endRadius = dialRadius - 40
            val startX = centerX + startRadius * cos(angleRad).toFloat()
            val startY = centerY + startRadius * sin(angleRad).toFloat()
            val endX = centerX + endRadius * cos(angleRad).toFloat()
            val endY = centerY + endRadius * sin(angleRad).toFloat()
            drawLine(Color.Black, Offset(startX, startY), Offset(endX, endY), strokeWidth = 2f)
        }

        // Draw zones
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 40f
                textAlign = android.graphics.Paint.Align.CENTER
            }
            canvas.nativeCanvas.drawText("RAIN", centerX - dialRadius / 2, centerY + dialRadius / 4, paint)
            canvas.nativeCanvas.drawText("CHANGE", centerX, centerY - dialRadius / 2, paint)
            canvas.nativeCanvas.drawText("FAIR", centerX + dialRadius / 2, centerY + dialRadius / 4, paint)
        }

        // Draw pointers
        val pressureAngle = (data.pressure - 960) * (270f / 90f) - 135
        val pressureAngleRad = Math.toRadians(pressureAngle.toDouble())
        val pressurePointerLength = dialRadius - 60
        val pressurePointerX = centerX + pressurePointerLength * cos(pressureAngleRad).toFloat()
        val pressurePointerY = centerY + pressurePointerLength * sin(pressureAngleRad).toFloat()
        drawLine(Color.Black, Offset(centerX, centerY), Offset(pressurePointerX, pressurePointerY), strokeWidth = 8f)

        val tendencyAngle = (data.tendency - 960) * (270f / 90f) - 135
        val tendencyAngleRad = Math.toRadians(tendencyAngle.toDouble())
        val tendencyPointerLength = dialRadius - 50
        val tendencyPointerX = centerX + tendencyPointerLength * cos(tendencyAngleRad).toFloat()
        val tendencyPointerY = centerY + tendencyPointerLength * sin(tendencyAngleRad).toFloat()
        drawLine(Color(0xFFD4AF37), Offset(centerX, centerY), Offset(tendencyPointerX, tendencyPointerY), strokeWidth = 4f)

        // Draw central hub
        drawCircle(Color.Black, radius = 10f, center = Offset(centerX, centerY))

        // Draw text
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 20f
                textAlign = android.graphics.Paint.Align.CENTER
            }
            canvas.nativeCanvas.drawText("MILLIMETRES", centerX, centerY + dialRadius - 40, paint)
            canvas.nativeCanvas.drawText("MILLIBARS", centerX, centerY + dialRadius - 20, paint)
        }
    }
}

@Preview
@Composable
fun BarometerScreenPreview() {
    BarometerScreen(BarometerData(980f, 1030f))
}
