package com.example.fallsync.ui.data.network

import com.example.fallsync.ui.model.Registro

object ApiService {
    suspend fun getRegistros(): List<Registro> {
        // Simulación de datos
        return listOf(
            Registro(1, "2025-01-03"),
            Registro(2, "2025-01-05")
        )
    }

    suspend fun deleteRegistro(id: Int): Boolean {
        // Simulación de éxito
        return true
    }
}
