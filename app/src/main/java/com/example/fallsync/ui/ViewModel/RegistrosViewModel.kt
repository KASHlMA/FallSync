package com.example.fallsync.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fallsync.data.RegistroRepository
import com.example.fallsync.ui.model.Registro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class RegistrosViewModel(
    private val repository: RegistroRepository = RegistroRepository()
) : ViewModel() {

    private val _registros = MutableStateFlow<List<Registro>>(emptyList())
    val registros: StateFlow<List<Registro>> = _registros

    private val _mensajeUsuario = MutableStateFlow<String?>(null)
    val mensajeUsuario: StateFlow<String?> = _mensajeUsuario

    init {
        fetchRegistros()
    }

    fun limpiarMensaje() {
        _mensajeUsuario.value = null
    }

    // GET
    fun fetchRegistros() {
        viewModelScope.launch {
            try {

                _registros.value = repository.getRegistros()
            } catch (e: IOException) {
                _mensajeUsuario.value = "⚠️ Error de conexión: Verifica tu internet"
            } catch (e: Exception) {
                _mensajeUsuario.value = "❌ Error desconocido: ${e.message}"
            }
        }
    }

    // DELETE
    fun deleteRegistro(id: Int) {
        viewModelScope.launch {
            try {
                val success = repository.deleteRegistro(id)
                if (success) {
                    fetchRegistros()
                    _mensajeUsuario.value = "🗑️ Registro eliminado correctamente"
                } else {
                    _mensajeUsuario.value = "⚠️ No se pudo eliminar (Error del servidor)"
                }
            } catch (e: IOException) {
                _mensajeUsuario.value = "⚠️ No hay internet para eliminar"
            }
        }
    }

    // POST (Crear)
    fun createRegistro(descripcion: String, fecha: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                val nuevoRegistro = repository.createRegistro(descripcion, fecha)
                if (nuevoRegistro != null) {
                    fetchRegistros()
                    _mensajeUsuario.value = "✅ Guardado exitosamente"
                    onSuccess(nuevoRegistro.id)
                } else {
                    _mensajeUsuario.value = "❌ Error al guardar. Intenta de nuevo."
                }
            } catch (e: IOException) {
                _mensajeUsuario.value = "⚠️ Error de conexión: No se pudo guardar"
            } catch (e: Exception) {
                _mensajeUsuario.value = "❌ Error inesperado: ${e.localizedMessage}"
            }
        }
    }

    // PUT (Actualizar)
    fun updateRegistro(id: Int, descripcion: String, fecha: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val success = repository.updateRegistro(id, descripcion, fecha)
                if (success) {
                    fetchRegistros()
                    _mensajeUsuario.value = "💾 Cambios guardados"
                    onSuccess()
                } else {
                    _mensajeUsuario.value = "⚠️ Error al actualizar (Servidor)"
                }
            } catch (e: IOException) {
                _mensajeUsuario.value = "⚠️ Sin conexión: No se pudo actualizar"
            }
        }
    }
}