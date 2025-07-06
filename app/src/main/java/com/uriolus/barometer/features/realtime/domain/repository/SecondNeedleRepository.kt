package com.uriolus.barometer.features.realtime.domain.repository

import kotlinx.coroutines.flow.Flow

interface SecondNeedleRepository {
    fun getSecondNeedleValue(): Flow<Float?>
    suspend fun setSecondNeedleValue(value: Float)
}
