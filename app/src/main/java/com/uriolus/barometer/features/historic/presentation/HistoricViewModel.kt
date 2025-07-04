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
import kotlinx.coroutines.launch

class HistoricViewModel(
    private val getAllPressureReadingsUseCase: GetAllPressureReadingsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HistoricState())
    val state: StateFlow<HistoricState> = _state.asStateFlow()

    init {
        loadHistoricData()
    }

    private fun loadHistoricData() {
        viewModelScope.launch {
            getAllPressureReadingsUseCase.exec()
                .run {
                    _state.update {
                        it.copy(
                            readings = this,
                            isLoading = false
                        )
                    }
                }
        }
    }
}
