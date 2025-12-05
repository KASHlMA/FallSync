package com.example.fallsync.ui

import android.R.attr.type
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fallsync.ui.screens.*
import com.example.fallsync.ui.viewmodel.FallSyncViewModel

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val fallVM: FallSyncViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // HOME → GET y DELETE
        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = fallVM
            )
        }

        // CREATE → POST
        composable("create") {
            CreateScreen(
                navController = navController,
                viewModel = fallVM
            )
        }

        // UPDATE → PUT/PATCH (con argumento id)
        composable(
            route = "update/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            UpdateScreen(
                navController = navController,
                viewModel = fallVM,
                itemId = id
            )
        }

        // Fall Detection Screen
        composable("fallDetection") {
            FallDetectionScreen(
                navController = navController
            )
        }
    }
}
