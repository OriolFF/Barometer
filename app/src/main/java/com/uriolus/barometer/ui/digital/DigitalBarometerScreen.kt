package com.uriolus.barometer.ui.digital

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uriolus.barometer.ui.BarometerData
import com.uriolus.barometer.ui.theme.BarometerTheme
import java.util.Locale

@Composable
fun DigitalBarometerScreen(data: BarometerData, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.Black)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = String.format(Locale.US, "%.2f", data.pressureMilliBars),
            fontFamily = FontFamily.Monospace,
            fontSize = 56.sp,
            color = Color(0xFF39FF14), // Neon Green
            style = TextStyle(
                shadow = Shadow(
                    color = Color(0xFF39FF14).copy(alpha = 0.7f),
                    offset = androidx.compose.ui.geometry.Offset(0f, 0f),
                    blurRadius = 12f
                )
            )
        )
    }
}

@Preview
@Composable
fun DigitalBarometerScreenPreview() {
    BarometerTheme {
        DigitalBarometerScreen(data = BarometerData(1013.25f, 1012.00f))
    }
}
