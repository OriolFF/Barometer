package com.uriolus.barometer.features.realtime.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uriolus.barometer.R
import com.uriolus.barometer.features.realtime.presentation.analog.AnalogBarometerScreen
import com.uriolus.barometer.features.realtime.presentation.digital.DigitalBarometerScreen
import com.uriolus.barometer.features.shared.presentation.PressureChart
import com.uriolus.barometer.features.shared.presentation.PressureReading
import org.koin.androidx.compose.koinViewModel

@Composable
fun BarometerScreen(
    modifier: Modifier = Modifier,
    viewModel: BarometerViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    BarometerScreen(
        modifier = modifier,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun BarometerScreen(
    modifier: Modifier = Modifier,
    state: BarometerViewState,
    onEvent: (BarometerEvent) -> Unit
) {

    val data = state.barometerData
    val isLoading = state.isLoading
    val pressureHistory = state.pressureHistory
    val isBackgroundMonitoringEnabled = state.isBackgroundMonitoringEnabled
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,

                ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 8.dp)
                        .background(MaterialTheme.colorScheme.background),

                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    DigitalBarometerScreen(
                        data = data,
                        modifier = Modifier,
                        isLoading = isLoading,
                        isBackgroundMonitoringEnabled = isBackgroundMonitoringEnabled,
                        onEvent = onEvent
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    AnalogBarometerScreen(
                        data = data,
                        modifier = Modifier.fillMaxWidth(0.6f),
                        onEvent = { onEvent(BarometerEvent.OnBarometerResetTendency) },
                        isLoading = isLoading
                    )
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = stringResource(id = R.string.pressure_trend),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        PressureChart(
                            readings = pressureHistory,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        else -> { // Portrait
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DigitalBarometerScreen(
                    data = data,
                    modifier = Modifier,
                    isLoading = isLoading,
                    isBackgroundMonitoringEnabled = isBackgroundMonitoringEnabled,
                    onEvent = onEvent
                )
                Spacer(modifier = Modifier.height(16.dp))
                AnalogBarometerScreen(
                    data = data,
                    modifier = Modifier.fillMaxWidth(0.95f),
                    onEvent = { onEvent(BarometerEvent.OnBarometerResetTendency) },
                    isLoading = isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = stringResource(id = R.string.pressure_trend),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        PressureChart(
                            readings = pressureHistory,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Barometer Screen Light theme")
@Preview(name = "Barometer Screen Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(
    name = "Barometer Screen Landscape",
    device = "spec:width=411dp,height=891dp,dpi=420,orientation=landscape"
)
@Composable
fun BarometerScreenPreview() {
    BarometerScreen(
        state = BarometerViewState(
            barometerData = BarometerData(
                pressureMilliBars = 1013.25f,
                tendencyMilliBars = 1015f
            ),
            pressureHistory = listOf(
                PressureReading(System.currentTimeMillis() - 10000, 1012f),
                PressureReading(System.currentTimeMillis() - 5000, 1014f),
                PressureReading(System.currentTimeMillis(), 1013.25f)
            ),
            isLoading = false,
            isBackgroundMonitoringEnabled = true
        ),
        onEvent = {}
    )
}
