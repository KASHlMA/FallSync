package com.example.fallsync.data

import com.example.fallsync.data.network.RetrofitInstance
import com.example.fallsync.ui.model.Registro
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class RegistroRepository {

    private val api = RetrofitInstance.api

    // GET
    suspend fun getRegistros(): List<Registro> {
        return try {
            val response = api.getRegistros()
            if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // DELETE
    suspend fun deleteRegistro(id: Int): Boolean {
        return try {
            val response = api.deleteRegistro(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    // POST (Crear) - Ahora devuelve el Registro creado (o null si falla) para obtener su ID
    suspend fun createRegistro(descripcion: String, fecha: String): Registro? {
        return try {
            val descPart = toRequestBody(descripcion)
            val fechaPart = toRequestBody(fecha)

            val response = api.createRegistro(descPart, fechaPart)
            if (response.isSuccessful) {
                response.body() // Devolvemos el objeto completo (con ID)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // PUT (Actualizar) - Maneja la conversión a Multipart
    suspend fun updateRegistro(id: Int, descripcion: String, fecha: String): Boolean {
        return try {
            val descPart = toRequestBody(descripcion)
            val fechaPart = toRequestBody(fecha)

            val response = api.updateRegistro(id, descPart, fechaPart)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Función auxiliar privada para crear los RequestBody
    private fun toRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}