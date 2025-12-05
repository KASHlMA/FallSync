package com.example.fallsync.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.fallsync.data.model.FallItem
import com.example.fallsync.data.repository.FallRepository

class FallSyncViewModel(
    application: Application,
    private val repository: FallRepository
) : AndroidViewModel(application) {

    // ---------- GET (StateFlow automáticamente observable) ----------
    val allFalls: StateFlow<List<FallItem>> =
        repository.getAllFalls()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // ---------- POST ----------
    fun addFall(title: String) {
        viewModelScope.launch {
            try {
                val fall = FallItem(
                    id = 0,               // el servidor genera ID
                    title = title,
                    timestamp = System.currentTimeMillis()
                )
                repository.insertFall(fall)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ---------- DELETE ----------
    fun deleteFall(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteFall(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ---------- PUT / PATCH ----------
    fun updateFall(id: Int, newTitle: String) {
        viewModelScope.launch {
            try {
                repository.updateFall(
                    FallItem(
                        id = id,
                        title = newTitle,
                        timestamp = System.currentTimeMillis()
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ---------- Obtener 1 item ----------
    fun getFallById(id: Int): FallItem? {
        return allFalls.value.firstOrNull { it.id == id }
    }
}
