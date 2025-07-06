package com.uriolus.barometer.features.secondneedle.data.repository

import com.uriolus.barometer.features.secondneedle.data.datasource.SecondNeedleLocalDataSource
import com.uriolus.barometer.features.secondneedle.domain.repository.SecondNeedleRepository
import kotlinx.coroutines.flow.Flow

class SecondNeedleRepositoryImpl(
    private val localDataSource: SecondNeedleLocalDataSource
) : SecondNeedleRepository {
    override fun getSecondNeedleValue(): Flow<Float?> = localDataSource.secondNeedleValue

    override suspend fun setSecondNeedleValue(value: Float) {
        localDataSource.setSecondNeedleValue(value)
    }
}
