package com.uriolus.barometer.features.historic.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.barometer.features.historic.domain.usecases.GetAllPressureReadingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class HistoricViewModel(
    private val getAllPressureReadingsUseCase: GetAllPressureReadingsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HistoricState())
    val state: StateFlow<HistoricState> = _state.asStateFlow()

    init {
        loadHistoricData()
    }

    private fun loadHistoricData() {
        getAllPressureReadingsUseCase.exec()
            .onEach { readings ->
                _state.update {
                    it.copy(
                        readings = readings,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}
