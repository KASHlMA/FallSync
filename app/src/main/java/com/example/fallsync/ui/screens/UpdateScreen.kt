package com.example.fallsync.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fallsync.ui.viewmodel.RegistrosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    navController: NavController,
    registroId: Int,
    viewModel: RegistrosViewModel = viewModel()
) {
    val registros by viewModel.registros.collectAsState()

    val registroActual = remember(registros, registroId) {
        registros.find { it.id == registroId }
    }

    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    LaunchedEffect(registroActual) {
        registroActual?.let {
            descripcion = it.descripcion
            fecha = it.fecha
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Registro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Descripción:", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Ej: Caída en escaleras") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Fecha:", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("YYYY-MM-DD") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (descripcion.isNotEmpty() && fecha.isNotEmpty()) {
                        viewModel.updateRegistro(registroId, descripcion, fecha) {
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = descripcion.isNotEmpty() && fecha.isNotEmpty()
            ) {
                Text("ACTUALIZAR")
            }
        }
    }
}