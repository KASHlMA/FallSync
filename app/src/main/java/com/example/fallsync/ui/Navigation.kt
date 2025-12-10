package com.example.fallsync.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fallsync.ui.screens.AccelerometerScreen


import com.example.fallsync.ui.screens.HomeScreen
import com.example.fallsync.ui.screens.RegistrosScreen

//import com.example.fallsync.ui.screens.CreateScreen
//import com.example.fallsync.ui.screens.UpdateScreen
//import com.example.fallsync.ui.screens.FallDetectionScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        // HOME
        composable("home") {
            HomeScreen(navController = navController)
        }
        // REGISTROS
       composable("registros") {
           RegistrosScreen(navController = navController)
        }


        // CREATE
      //  composable("create") {
       //     CreateScreen(navController = navController)
      //  }

        // UPDATE — ejemplo sin argumentos
        //   composable("update") {
        //      UpdateScreen(navController = navController, itemId = "")
        //   }

        // FALL DETECTION
        //  composable("fallDetection") {
        //      FallDetectionScreen(navController = navController)
        //   }
        // ACCELEROMETER
        composable("accelerometer") {
            AccelerometerScreen()
        }
    }
}
