package com.example.fallsync.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel // Importante
import com.example.fallsync.ui.viewmodel.RegistrosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    navController: NavController,
    viewModel: RegistrosViewModel = viewModel()
) {
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) } // Para mostrar carga

    // Contenedor principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Nuevo Registro", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        // Campo Descripción
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción (Ej: Caída en sala)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Campo Fecha
        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        // BOTÓN GUARDAR
        Button(
            onClick = {
                isSaving = true
                // Llamamos a la función createRegistro del ViewModel
                viewModel.createRegistro(descripcion, fecha) {
                    isSaving = false
                    navController.popBackStack() // Regresa a la lista al terminar
                }
            },
            enabled = !isSaving && descripcion.isNotEmpty() && fecha.isNotEmpty(), // Se desactiva si está vacío
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("GUARDAR REGISTRO")
            }
        }
    }
}