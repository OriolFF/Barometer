package com.uriolus.barometer.features.realtime.data.repository

import com.uriolus.barometer.features.realtime.data.datasource.SecondNeedleDataSource
import com.uriolus.barometer.features.realtime.domain.repository.SecondNeedleRepository
import kotlinx.coroutines.flow.Flow

class SecondNeedleRepositoryImpl(
    private val dataSource: SecondNeedleDataSource
) : SecondNeedleRepository {
    override fun getSecondNeedleValue(): Flow<Float?> = dataSource.secondNeedleValue

    override suspend fun setSecondNeedleValue(value: Float) {
        dataSource.setSecondNeedleValue(value)
    }
}
