package com.uriolus.barometer.features.realtime.presentation.digital

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uriolus.barometer.R
import com.uriolus.barometer.features.realtime.presentation.BarometerData
import com.uriolus.barometer.features.realtime.presentation.theme.BarometerTheme
import java.util.Locale

@Composable
fun DigitalBarometerScreen(
    data: BarometerData,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val text = if (isLoading) {
            stringResource(id = R.string.waiting_data)
        } else {
            String.format(Locale.US, "%.2f", data.pressureMilliBars)
        }
        Text(
            text = text,
            style = TextStyle(
                fontFamily = MaterialTheme.typography.displayLarge.fontFamily,
                fontSize = 48.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DigitalBarometerScreenPreview() {
    BarometerTheme {
        DigitalBarometerScreen(data = BarometerData(1013.25f, 1012.24f), isLoading = false)
    }
}

@Preview(showBackground = true)
@Composable
fun DigitalBarometerScreenLoadingPreview() {
    BarometerTheme {
        DigitalBarometerScreen(data = BarometerData(1013.25f, 1012.24f), isLoading = true)
    }
}
