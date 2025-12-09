package com.example.fallsync.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    navController: NavController,
    id: Int,
    initialDescripcion: String,
    initialFecha: String
) {

    var descripcion by remember { mutableStateOf(initialDescripcion) }
    var fecha by remember { mutableStateOf(initialFecha) }

    val client = HttpClient()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Editar registro") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text("Descripción:")
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Fecha:")
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        try {
                            client.put("http:") {
                                contentType(ContentType.Application.Json)
                                setBody(
                                    mapOf(
                                        "descripcion" to descripcion,
                                        "fecha" to fecha
                                    )
                                )
                            }

                            navController.popBackStack()

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("ACTUALIZAR")
            }
        }
    }
}
