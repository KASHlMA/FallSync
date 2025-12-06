package com.example.fallsync.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf("Inicio", "Registros", "Crear", "Caídas")
    val icons = listOf(Icons.Default.Home, Icons.Default.List, Icons.Default.Add, Icons.Default.Warning)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FallSync", fontWeight = FontWeight.Bold) },
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 6.dp
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create") },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear")
            }
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.primarySurface,
                elevation = 10.dp
            ) {
                items.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            when(index) {
                                0 -> navController.navigate("home")
                                1 -> navController.navigate("list")
                                2 -> navController.navigate("create")
                                3 -> navController.navigate("fallDetection")
                            }//
                        }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF2196F3), Color(0xFFBBDEFB))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            }
        }
    }
}
