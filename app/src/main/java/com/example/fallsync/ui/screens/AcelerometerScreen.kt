package com.example.fallsync.ui.screens


import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fallsync.ui.viewmodel.AccelerometerViewModel

@Composable
fun AccelerometerScreen(viewModel: AccelerometerViewModel = viewModel()) {
    val acceleration by viewModel.acceleration.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.start()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stop()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("X: ${acceleration.first}")
        Text("Y: ${acceleration.second}")
        Text("Z: ${acceleration.third}")
    }
}
