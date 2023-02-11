    package com.example.accelerometer
    
    import android.graphics.Color
    import android.hardware.Sensor
    import android.hardware.SensorEvent
    import android.hardware.SensorEventListener
    import android.hardware.SensorManager
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatDelegate
    
    class MainActivity : AppCompatActivity(), SensorEventListener {
    
        private lateinit var sensorMngr: SensorManager
        private lateinit var cube: TextView
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
    
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    
            cube = findViewById(R.id.cubeText)
    
            setUpSensor()
        }
    
        private fun setUpSensor() {
    
            sensorMngr = getSystemService(SENSOR_SERVICE) as SensorManager
            sensorMngr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
                sensorMngr.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_FASTEST,
                    SensorManager.SENSOR_DELAY_FASTEST
                )
            }
        }
    
        override fun onSensorChanged(event: SensorEvent?) {
    
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
    
                val sides = event.values[0]
                val frontBack = event.values[2]
    
                cube.apply {
                    rotationX = frontBack * 3f
                    rotationY = sides * 3f
                    rotation = -sides
                    translationX = sides * -10
                    translationY = frontBack * 10
                }
    
    
                val color = if (frontBack.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.RED
                cube.setBackgroundColor(color)
    
                cube.text = "front/back ${frontBack.toInt()}\nleft/right ${sides.toInt()}"
            }
        }
    
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            return
        }
    
        override fun onDestroy() {
            sensorMngr.unregisterListener(this)
            super.onDestroy()
        }
    }