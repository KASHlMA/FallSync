package com.example.fallsync.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image // Importar Image para el drawable
import androidx.compose.ui.res.painterResource // Importar painterResource
import com.example.fallsync.R // Asegúrate de que esta sea la ruta correcta a tu clase R

@Composable
fun HomeScreen(
    onStartDetectionClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2196F3), Color(0xFFBBDEFB))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 1. DIBUJABLE AGREGADO AQUÍ
            Image(
                // Reemplaza R.drawable.ic_fall_detection con el nombre real de tu recurso
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Ícono de Detección de Caídas",
                modifier = Modifier
                    .size(120.dp) // Define el tamaño del ícono
                    .padding(bottom = 24.dp)
            )

            Text(
                "Bienvenido a FallSync",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Tu app para gestionar y detectar caídas",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStartDetectionClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Iniciar detección",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}