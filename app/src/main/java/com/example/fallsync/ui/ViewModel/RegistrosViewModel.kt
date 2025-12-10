package com.example.fallsync.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fallsync.data.RegistroRepository
import com.example.fallsync.ui.model.Registro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrosViewModel(
    // Instanciamos el repositorio aquí
    private val repository: RegistroRepository = RegistroRepository()
) : ViewModel() {

    private val _registros = MutableStateFlow<List<Registro>>(emptyList())
    val registros: StateFlow<List<Registro>> = _registros

    init {
        fetchRegistros()
    }

    // GET
    fun fetchRegistros() {
        viewModelScope.launch {
            _registros.value = repository.getRegistros()
        }
    }

    // DELETE
    fun deleteRegistro(id: Int) {
        viewModelScope.launch {
            val success = repository.deleteRegistro(id)
            if (success) fetchRegistros() // Recargamos la lista si se borró bien
        }
    }

    // POST (Modificado para devolver el ID)
    fun createRegistro(descripcion: String, fecha: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            val nuevoRegistro = repository.createRegistro(descripcion, fecha)
            if (nuevoRegistro != null) {
                fetchRegistros() // Recargar lista para que aparezca en todos lados
                onSuccess(nuevoRegistro.id) // ¡Aquí pasamos el ID nuevo!
            }
        }
    }

    // PUT (Nuevo)
    fun updateRegistro(id: Int, descripcion: String, fecha: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val success = repository.updateRegistro(id, descripcion, fecha)
            if (success) {
                fetchRegistros()
                onSuccess()
            }
        }
    }
}