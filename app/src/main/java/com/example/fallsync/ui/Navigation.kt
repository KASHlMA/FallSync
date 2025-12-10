package com.example.fallsync.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fallsync.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    // Detectamos la ruta actual para iluminar el ícono de abajo
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        // 1. BARRA SUPERIOR
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FallSync", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        // 2. HEMOS QUITADO EL BOTÓN FLOTANTE (+) AQUÍ

        // 3. BARRA INFERIOR
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
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
                            // LÓGICA CORREGIDA PARA QUE NO SE TRABE
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    // 1. Limpia todo hasta llegar a Home para no acumular pantallas
                                    popUpTo("home") {
                                        // Si vas a Home, no guardes estado, reinicia limpio
                                        saveState = false
                                    }
                                    // 2. Evita abrir la misma pantalla dos veces
                                    launchSingleTop = true
                                    // 3. Quitamos restoreState para evitar que cargue una pantalla "congelada"
                                }
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
    ) { paddingValues ->
        // 4. NAVHOST
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            // HOME
            composable("home") {
                HomeScreen(onStartDetectionClick = { navController.navigate("fallDetection") })
            }

            // REGISTROS
            composable("registros") {
                RegistrosScreen(navController = navController)
            }

            // CREATE
            composable("create") {
                CreateScreen(navController = navController)
            }

            // SENSOR
            composable("fallDetection") {
                FallDetectionScreen(navController = navController)
            }

            // UPDATE
            composable(
                route = "update/{registroId}",
                arguments = listOf(navArgument("registroId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("registroId") ?: 0
                UpdateScreen(navController = navController, registroId = id)
            }
        }
    }
}