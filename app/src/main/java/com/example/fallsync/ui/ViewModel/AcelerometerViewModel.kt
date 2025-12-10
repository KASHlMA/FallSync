package com.example.fallsync.ui.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.fallsync.ui.sensors.AccelerometerManager

class AccelerometerViewModel(application: Application) : AndroidViewModel(application) {
    val accelerometerManager = AccelerometerManager(application)

    val acceleration = accelerometerManager.acceleration

    fun start() = accelerometerManager.startListening()
    fun stop() = accelerometerManager.stopListening()
}
