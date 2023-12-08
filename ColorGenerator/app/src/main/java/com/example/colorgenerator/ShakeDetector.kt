package com.example.colorgenerator

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.lang.StrictMath.sqrt
import kotlin.math.sqrt

class ShakeDetector : SensorEventListener {
    private var mListener: OnShakeListener? = null
    private var mShakeTimestamp: Long = 0
    private var mShakeCount = 0
    fun setOnShakeListener(listener: OnShakeListener?) {
        mListener = listener
    }

    interface OnShakeListener {
        fun onShake(count: Int)
    }

    override fun onAccuracyChanged(
        sensor: Sensor,
        accuracy: Int
    ) { // ignore
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (mListener != null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH
            // gForce will be close to 1 when there is no movement.
            val gForce: Float = sqrt(gX * gX + gY * gY + gZ * gZ) as Float
            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                val now = System.currentTimeMillis()
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return
                }
                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0
                }
                mShakeTimestamp = now
                mShakeCount++
                mListener!!.onShake(mShakeCount)
            }
        }
    }

    companion object {

        private const val SHAKE_THRESHOLD_GRAVITY = 2.7f
        private const val SHAKE_SLOP_TIME_MS = 500
        private const val SHAKE_COUNT_RESET_TIME_MS = 3000
    }
}