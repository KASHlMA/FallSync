package com.example.fallsync.ui.data.repository

import com.example.fallsync.ui.data.network.RetrofitInstance
import com.example.fallsync.ui.model.Registro

class RegistroRepository {
    private val api = RetrofitInstance.api

    suspend fun getRegistros(): List<Registro> {
        val response = api.getRegistros()
        if (response.isSuccessful) {
            return response.body().orEmpty()
        } else {
            throw Exception("Error GET: ${response.code()} ${response.message()}")
        }
    }

    suspend fun deleteRegistro(id: Int): Boolean {
        val response = api.deleteRegistro(id)
        return response.isSuccessful
    }
}
