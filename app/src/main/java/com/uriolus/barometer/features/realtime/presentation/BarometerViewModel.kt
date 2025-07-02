package com.uriolus.barometer.features.realtime.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.barometer.features.historic.domain.usecases.GetAllPressureReadingsUseCase
import com.uriolus.barometer.features.realtime.domain.usecases.StartBarometerUseCase
import com.uriolus.barometer.features.realtime.domain.usecases.StopBarometerUseCase
import com.uriolus.barometer.features.realtime.domain.usecases.SubscribeBarometerUseCase
import com.uriolus.barometer.features.shared.presentation.PressureReading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BarometerViewModel(
    private val startBarometerUseCase: StartBarometerUseCase,
    private val stopBarometerUseCase: StopBarometerUseCase,
    private val subscribeBarometerUseCase: SubscribeBarometerUseCase,
    private val getAllPressureReadingsUseCase: GetAllPressureReadingsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BarometerViewState())
    val state: StateFlow<BarometerViewState> = _state.asStateFlow()

    init {
        loadHistoricData()
        start()
        subscribeToBarometer()
    }

    private fun loadHistoricData() {
        viewModelScope.launch {
            getAllPressureReadingsUseCase.exec()
                .flowOn(Dispatchers.IO)
                .collect { samples ->
                    val history = samples.map {
                        PressureReading(it.timestamp, it.pressureValue)
                    }
                    _state.update { it.copy(pressureHistory = history) }
                }
        }
    }

    private fun start() {
        startBarometerUseCase()
    }

    private fun stop() {
        stopBarometerUseCase()
    }

    private fun subscribeToBarometer() {
        subscribeBarometerUseCase.exec()
            .onEach { reading ->
                _state.update { currentState ->
                    // For now, tendency is the previous pressure reading
                    Log.d("BarometerViewModel", "New data: $reading")
                    val newData = BarometerData(
                        pressureMilliBars = reading.pressure,
                        tendencyMilliBars = currentState.barometerData.pressureMilliBars
                    )
                    val newHistoryReading = PressureReading(
                        timestamp = System.currentTimeMillis(),
                        pressure = reading.pressure
                    )
                    val updatedHistory = (currentState.pressureHistory + newHistoryReading).takeLast(100)
                    currentState.copy(
                        barometerData = newData,
                        isLoading = false,
                        pressureHistory = updatedHistory
                    )
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        stop()
        super.onCleared()
    }
}
