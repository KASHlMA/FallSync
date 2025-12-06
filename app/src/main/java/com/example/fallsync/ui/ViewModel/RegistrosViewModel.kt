package com.example.fallsync.ui.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fallsync.ui.model.Registro
import com.example.fallsync.ui.data.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrosViewModel : ViewModel() {
    private val _registros = MutableStateFlow<List<Registro>>(emptyList())
    val registros: StateFlow<List<Registro>> = _registros

    init {
        fetchRegistros()
    }

    fun fetchRegistros() {
        viewModelScope.launch {
            val response = ApiService.getRegistros()
            _registros.value = response
        }
    }

    fun deleteRegistro(id: Int) {
        viewModelScope.launch {
            val success = ApiService.deleteRegistro(id)
            if (success) fetchRegistros()
        }
    }
}
