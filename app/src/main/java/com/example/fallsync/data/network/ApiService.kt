package com.example.fallsync.data.network

import com.example.fallsync.ui.model.Registro
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("registros")
    suspend fun getRegistros(): Response<List<Registro>>

    @Multipart
    @POST("registros")
    suspend fun createRegistro(
        @Part("descripcion") descripcion: RequestBody,
        @Part("fecha") fecha: RequestBody
    ): Response<Registro>

    @Multipart
    @PUT("registros/{id}")
    suspend fun updateRegistro(
        @Path("id") id: Int,
        @Part("descripcion") descripcion: RequestBody,
        @Part("fecha") fecha: RequestBody
    ): Response<Registro>

    @DELETE("registros/{id}")
    suspend fun deleteRegistro(@Path("id") id: Int): Response<Unit>
}