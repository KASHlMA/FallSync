package com.example.fallsync.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fallsync.data.model.Item
import com.example.fallsync.viewmodel.ItemViewModel

@Composable
fun HomeScreen(
    viewModel: ItemViewModel,
    navController: NavController
) {
    val items by viewModel.items.collectAsState()

    // Cargar datos una sola vez
    LaunchedEffect(Unit) {
        viewModel.loadItems()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FallSync • Datos registrados") },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->

        if (items.isEmpty()) {
            // Mensaje si no hay items
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay registros todavía")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(12.dp)
            ) {
                items(items) { item ->
                    ItemCard(
                        item = item,
                        onEdit = {
                            navController.navigate("update/${item.id}")
                        },
                        onDelete = {
                            item.id?.let { viewModel.deleteItem(it) }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ItemCard(
    item: Item,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        elevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = item.nombre,
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.descripcion,
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}
