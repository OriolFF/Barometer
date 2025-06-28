package com.uriolus.barometer.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.barometer.domain.usecases.StartBarometerUseCase
import com.uriolus.barometer.domain.usecases.StopBarometerUseCase
import com.uriolus.barometer.domain.usecases.SubscribeBarometerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class BarometerViewModel(
    private val startBarometerUseCase: StartBarometerUseCase,
    private val stopBarometerUseCase: StopBarometerUseCase,
    private val subscribeBarometerUseCase: SubscribeBarometerUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BarometerViewState())
    val state: StateFlow<BarometerViewState> = _state.asStateFlow()

    init {
        start()
        subscribeToBarometer()
    }

    private fun start() {
        startBarometerUseCase()
    }

    private fun stop() {
        stopBarometerUseCase()
    }

    private fun subscribeToBarometer() {
        subscribeBarometerUseCase()
            .onEach { reading ->
                _state.update { currentState ->
                    // For now, tendency is the previous pressure reading
                    Log.d("BarometerViewModel", "New data: $reading")
                    val newData = BarometerData(
                        pressureMilliBars = reading.pressure,
                        tendencyMilliBars = currentState.barometerData.pressureMilliBars
                    )
                    currentState.copy(
                        barometerData = newData,
                        isLoading = false
                    )

                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        stop()
        super.onCleared()
    }
}
