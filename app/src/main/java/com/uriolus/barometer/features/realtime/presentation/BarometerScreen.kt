package com.uriolus.barometer.features.realtime.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.uriolus.barometer.features.realtime.presentation.analog.AnalogBarometerScreen
import com.uriolus.barometer.features.realtime.presentation.digital.DigitalBarometerScreen
import com.uriolus.barometer.features.shared.presentation.PressureChart
import org.koin.androidx.compose.koinViewModel


@Composable
fun BarometerScreen(
    modifier: Modifier = Modifier,
    viewModel: BarometerViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    BarometerScreen(
        modifier = modifier,
        state = state
    )
}

@Composable
private fun BarometerScreen(
    modifier: Modifier = Modifier,
    state: BarometerViewState
) {

    val data = state.barometerData
    val isLoading = state.isLoading
    val pressureHistory = state.pressureHistory
    val configuration = LocalConfiguration.current

    if (isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        DigitalBarometerScreen(
                            data = data,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        AnalogBarometerScreen(
                            data = data,
                            modifier = Modifier.fillMaxWidth()
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
                                text = "Pressure Trend",
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
                    modifier = modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    DigitalBarometerScreen(
                        data = data,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    AnalogBarometerScreen(
                        data = data,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Pressure chart section
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "Pressure Trend",
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
}
