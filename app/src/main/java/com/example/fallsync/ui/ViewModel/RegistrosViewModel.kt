package com.example.fallsync.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fallsync.ui.data.repository.RegistroRepository
import com.example.fallsync.ui.modela.Registro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrosViewModel(
    private val repository: RegistroRepository = RegistroRepository()
) : ViewModel() {

    private val _registros = MutableStateFlow<List<Registro>>(emptyList())
    val registros: StateFlow<List<Registro>> = _registros

    init {
        fetchRegistros()
    }

    fun fetchRegistros() {
        viewModelScope.launch {
            try {
                _registros.value = repository.getRegistros()
            } catch (e: Exception) {
                _registros.value = emptyList()
            }
        }
    }

    fun deleteRegistro(id: Int) {
        viewModelScope.launch {
            val success = repository.deleteRegistro(id)
            if (success) fetchRegistros()
        }
    }
}
