package com.example.fallsync.ui

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fallsync.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    // Detectamos en qué pantalla estamos para iluminar el icono correcto
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        // 1. BARRA SUPERIOR FIJA
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FallSync", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        // 2. BOTÓN FLOTANTE FIJO (Opcional: puedes ocultarlo en ciertas pantallas si quieres)
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create") },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear")
            }
        },
        // 3. BARRA INFERIOR FIJA
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
                // Definimos los items del menú
                val items = listOf(
                    Triple("home", "Inicio", Icons.Default.Home),
                    Triple("registros", "Registros", Icons.Default.List),
                    Triple("fallDetection", "Sensor", Icons.Default.Warning)
                )

                items.forEach { (route, label, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = currentRoute == route,
                        onClick = {
                            navController.navigate(route) {
                                // Esto evita que se amontonen pantallas al navegar
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(paddingValues) // Importante para respetar las barras
    ) {

        // 1. HOME
        // Le pasamos la acción para navegar al sensor
        composable("home") {
            HomeScreen(onStartDetectionClick = { navController.navigate("fallDetection") })
        }

        // 2. REGISTROS (Lista)
        composable("registros") {
            RegistrosScreen(navController = navController)
        }

        // 3. CREATE (Guardar nuevo)
        composable("create") {
            CreateScreen(navController = navController)
        }

        // 4. SENSOR (Acelerómetro)
        composable("fallDetection") {
            FallDetectionScreen(navController = navController)
        }

        // 5. UPDATE (Editar registro existente)
        composable(
            route = "update/{registroId}",
            arguments = listOf(navArgument("registroId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Recuperamos el ID que viene en la URL interna
            val id = backStackEntry.arguments?.getInt("registroId") ?: 0

            // Llamamos a la pantalla pasando el ID
            UpdateScreen(navController = navController, registroId = id)
        }
    }

    }

}