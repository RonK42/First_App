package com.example.hm_1.Utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.hm_1.Interface.TiltCallback
import kotlin.math.abs


class TiltDetector(context: Context, private val tiltCallback: TiltCallback?) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor
    private lateinit var sensorEventListener: SensorEventListener

    var tiltCounterX: Int = 0
        private set

    var tiltCounterY: Int = 0
        private set

    private var timestamp: Long = 0L

    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                calculateTilt(x,y)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // pass
            }
        }
    }

    private fun calculateTilt(x: Float, y: Float) {
        val stillThreshold = 2f

        if (System.currentTimeMillis() - timestamp >= 50) {
            timestamp = System.currentTimeMillis()

            if (abs(x) > stillThreshold) {
                if (x > 1.5) {
                    tiltCounterX--
                    tiltCallback?.tiltX()
                } else if (x < -1.5) {
                    tiltCounterX++
                    tiltCallback?.tiltX()
                }
            }

            if (abs(y) > stillThreshold) {
                if (y > 1.5) {
                    tiltCounterY++
                    tiltCallback?.tiltY()
                } else if (y < -1.5) {
                    tiltCounterY--
                    tiltCallback?.tiltY()
                }
            }
        }
    }

    fun start(){
        sensorManager
            .registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
    }



    fun stop(){
        sensorManager
            .unregisterListener(
                sensorEventListener,
                sensor
            )
    }

}