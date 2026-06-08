package com.ute.restauranteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.restauranteapp.domain.model.Cliente
import com.ute.restauranteapp.domain.repository.ClienteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ClienteState {
    data object Loading : ClienteState
    data class Success(
        val data: List<Cliente>,
        val total: Int
    ) : ClienteState
    data class Error(val message: String) : ClienteState
}

@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val repository: ClienteRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ClienteState>(ClienteState.Loading)
    val state: StateFlow<ClienteState> = _state.asStateFlow()

    fun loadClientes(
        search: String? = null,
        correo: String? = null,
        page: Int? = null
    ) {
        viewModelScope.launch {
            _state.value = ClienteState.Loading

            repository.getClientes(
                search = search,
                correo = correo,
                page = page
            ).onSuccess { result ->

                val clientes = result.first
                val total = result.second

                _state.value = ClienteState.Success(
                    data = clientes,
                    total = total
                )

            }.onFailure { error ->

                _state.value = ClienteState.Error(
                    error.message ?: "Error al cargar clientes"
                )
            }
        }
    }
}