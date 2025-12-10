package com.example.fallsync.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fallsync.ui.viewmodel.RegistrosViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sqrt

@Composable
fun FallDetectionScreen(
    navController: NavController,
    viewModel: RegistrosViewModel = viewModel()
) {
    val context = LocalContext.current

    var isDetectionActive by remember { mutableStateOf(false) }
    var isFallDetected by remember { mutableStateOf(false) }
    var sensorValues by remember { mutableStateOf("X: 0 | Y: 0 | Z: 0") }

    // Manejo del Sensor
    DisposableEffect(isDetectionActive) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]

                    sensorValues = "X: ${"%.1f".format(x)} Y: ${"%.1f".format(y)} Z: ${"%.1f".format(z)}"

                    val gForce = sqrt(x * x + y * y + z * z) / 9.81

                    // DETECCIÓN DE CAÍDA (> 3G)
                    if (gForce > 3.0 && isDetectionActive && !isFallDetected) {
                        isFallDetected = true
                        isDetectionActive = false // Detenemos para no registrar doble

                        // 1. Preparamos los datos automáticos
                        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val horaActual = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        val descripcionAuto = "⚠️ Caída detectada a las $horaActual"

                        // 2. Guardamos y NAVEGAMOS AL UPDATE
                        viewModel.createRegistro(descripcionAuto, fechaActual) { nuevoId ->
                            // Esta parte se ejecuta cuando el servidor confirma el guardado
                            navController.navigate("update/$nuevoId")
                        }
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (isDetectionActive && accelerometer != null) {
            sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    // --- Diseño Visual ---
    val backgroundColor = if (isFallDetected) Color(0xFFFFEBEE) else Color.White
    val statusColor = if (isFallDetected) Color.Red else if (isDetectionActive) Color(0xFF4CAF50) else Color.Gray
    val statusText = if (isFallDetected) "¡CAÍDA REGISTRADA!" else if (isDetectionActive) "🟢 Monitoreando..." else "⚪ Sensor Inactivo"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(statusText, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = statusColor)

        Spacer(modifier = Modifier.height(30.dp))

        // Animación visual del sensor
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(statusColor.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(statusColor, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text("Acelerómetro:", style = MaterialTheme.typography.labelLarge)
        Text(
            text = sensorValues,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(40.dp))

        if (isFallDetected) {
            Text("Redirigiendo al registro...", fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        } else {
            Button(
                onClick = { isDetectionActive = !isDetectionActive },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDetectionActive) Color.Red else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(if (isDetectionActive) "DETENER" else "ACTIVAR DETECCIÓN")
            }
        }
    }
}