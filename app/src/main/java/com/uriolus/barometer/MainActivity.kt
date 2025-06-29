package com.uriolus.barometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.uriolus.barometer.features.realtime.presentation.BarometerData
import com.uriolus.barometer.features.realtime.presentation.BarometerScreen
import com.uriolus.barometer.features.realtime.presentation.BarometerViewModel
import com.uriolus.barometer.features.realtime.presentation.theme.BarometerTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BarometerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: BarometerViewModel = koinViewModel()
                    val state by viewModel.state.collectAsState()

                    BarometerScreen(
                        data = state.barometerData,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BarometerTheme {
        BarometerScreen(BarometerData(1025f, 1030f))
    }
}