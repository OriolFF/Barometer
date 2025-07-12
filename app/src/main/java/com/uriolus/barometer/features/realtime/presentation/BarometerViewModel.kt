package com.uriolus.barometer.features.realtime.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.barometer.background.worker.WorkScheduler
import com.uriolus.barometer.features.historic.domain.usecases.GetAllPressureReadingsUseCase
import com.uriolus.barometer.features.realtime.domain.usecases.GetSecondNeedleValueUseCase
import com.uriolus.barometer.features.realtime.domain.usecases.SetSecondNeedleValueUseCase
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
    private val getAllPressureReadingsUseCase: GetAllPressureReadingsUseCase,
    private val getSecondNeedleValueUseCase: GetSecondNeedleValueUseCase,
    private val setSecondNeedleValueUseCase: SetSecondNeedleValueUseCase,
    private val workScheduler: WorkScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(BarometerViewState())
    val state: StateFlow<BarometerViewState> = _state.asStateFlow()

    init {
        startBarometer()
        subscribeToBarometer()
        loadHistoricData()
        subscribeToSecondNeedle()
        subscribeToBackgroundMonitoringState()
    }

    fun onEvent(event: BarometerEvent) {
        when (event) {
            is BarometerEvent.OnBarometerResetTendency -> {
                viewModelScope.launch {
                    val currentPressure = _state.value.barometerData.pressureMilliBars
                    setSecondNeedleValueUseCase.exec(currentPressure)
                }
            }

            is BarometerEvent.OnToggleBackgroundMonitoring -> {
                toggleBackgroundMonitoring(event.enabled)
            }
        }
    }

    private fun toggleBackgroundMonitoring(enabled: Boolean) {
        if (enabled) {
            workScheduler.scheduleSensorWork()
        } else {
            workScheduler.cancelSensorWork()
        }
    }

    private fun loadHistoricData() {
        viewModelScope.launch {
            getAllPressureReadingsUseCase.exec().also {
                Log.d("BarometerViewModel", "Historic data: $it")
            }
                .let { samples ->
                    val history = samples.map {
                        PressureReading(it.timestamp, it.pressureValue)
                    }
                    _state.update { it.copy(pressureHistory = history) }
                }
        }
    }

    private fun startBarometer() {
        startBarometerUseCase()
    }

    private fun stop() {
        stopBarometerUseCase()
    }

    private fun subscribeToBarometer() {
        subscribeBarometerUseCase.exec()
            .onEach { reading ->
                _state.update { currentState ->
                    val newData = currentState.barometerData.copy(
                        pressureMilliBars = reading.pressure
                    )
                    currentState.copy(
                        barometerData = newData,
                        isLoading = false
                    )
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun subscribeToSecondNeedle() {
        getSecondNeedleValueUseCase.exec()
            .onEach { value ->
                _state.update {
                    it.copy(
                        barometerData = it.barometerData.copy(
                            tendencyMilliBars = value ?: it.barometerData.pressureMilliBars
                        )
                    )
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun subscribeToBackgroundMonitoringState() {
        workScheduler.isSensorWorkScheduled()
            .onEach { isRunning ->
                _state.update { it.copy(isBackgroundMonitoringEnabled = isRunning) }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        stop()
        super.onCleared()
    }
}
