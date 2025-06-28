package com.uriolus.barometer.util

object PressureConverter {
    private const val MBAR_TO_MMHG_FACTOR = 0.750062

    fun mbarToMmHg(mbar: Float): Float = mbar * MBAR_TO_MMHG_FACTOR.toFloat()

    fun mbarToMmHg(mbar: Int): Float = mbar * MBAR_TO_MMHG_FACTOR.toFloat()

    // cmHg is mmHg / 10
    fun mbarToCmHg(mbar: Float): Float = mbarToMmHg(mbar) / 10f

    fun mbarToCmHg(mbar: Int): Float = mbarToMmHg(mbar) / 10f

    fun cmHgToMbar(cmHg: Float): Float = (cmHg * 10f) / MBAR_TO_MMHG_FACTOR.toFloat()

    fun cmHgToMbar(cmHg: Int): Float = (cmHg * 10f) / MBAR_TO_MMHG_FACTOR.toFloat()
}
