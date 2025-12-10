package com.example.fallsync.ui.data.network


import com.example.fallsync.ui.modela.Registro
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.DELETE
import retrofit2.http.Path

interface ApiService {
    @GET("registros")
    suspend fun getRegistros(): Response<List<Registro>>

    @DELETE("registros/{id}")
    suspend fun deleteRegistro(@Path("id") id: Int): Response<Unit>
}
