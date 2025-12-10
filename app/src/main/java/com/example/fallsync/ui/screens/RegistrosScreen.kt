package com.example.fallsync.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Importante para que funcione items(registros)
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete

// CORRECCIÓN AQUÍ: viewmodel en minúsculas y Model en minúsculas (según tu estructura anterior)
import com.example.fallsync.ui.viewmodel.RegistrosViewModel
import com.example.fallsync.ui.model.Registro

@Composable
fun RegistrosScreen(
    navController: NavController,
    viewModel: RegistrosViewModel = viewModel()
) {
    val registros by viewModel.registros.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(registros) { registro ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Caída registrada", style = MaterialTheme.typography.titleMedium)
                        Row {
                            // Asegúrate de que registro.id exista en tu modelo
                            IconButton(onClick = {
                                navController.navigate("update/${registro.id}")
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = {
                                viewModel.deleteRegistro(registro.id)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Borrar")
                            }
                        }
                    }
                    Text(text = "Fecha: ${registro.fecha}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}