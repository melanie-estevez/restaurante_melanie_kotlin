package com.ute.restauranteapp.presentation.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.restauranteapp.domain.model.Cliente
import com.ute.restauranteapp.domain.model.ClientePayload
import com.ute.restauranteapp.domain.repository.ClienteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClienteAdminUiState(
    val isLoading: Boolean = false,
    val clientes: List<Cliente> = emptyList(),
    val selectedCliente: Cliente? = null,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class ClienteAdminViewModel @Inject constructor(
    private val repository: ClienteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ClienteAdminUiState())
    val state = _state.asStateFlow()

    init {
        cargarClientes()
    }

    fun cargarClientes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getClientes()
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            clientes = result.first,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al cargar clientes"
                        )
                    }
                }
        }
    }

    fun seleccionarCliente(cliente: Cliente?) {
        _state.update { it.copy(selectedCliente = cliente) }
    }

    fun guardarCliente(
        id: Int?,
        nombreCompleto: String,
        telefono: String,
        correo: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, successMessage = null) }

            val payload = ClientePayload(
                nombreCompleto = nombreCompleto,
                telefono = telefono,
                correo = correo
            )

            val result = if (id == null) {
                repository.createCliente(payload)
            } else {
                repository.updateCliente(id, payload)
            }

            result
                .onSuccess {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            successMessage = if (id == null) {
                                "Cliente creado correctamente"
                            } else {
                                "Cliente actualizado correctamente"
                            },
                            selectedCliente = null
                        )
                    }
                    cargarClientes()
                    onSuccess()
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al guardar cliente"
                        )
                    }
                }
        }
    }

    fun eliminarCliente(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, successMessage = null) }

            repository.deleteCliente(id)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Cliente eliminado correctamente"
                        )
                    }
                    cargarClientes()
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al eliminar cliente"
                        )
                    }
                }
        }
    }

    fun limpiarMensajes() {
        _state.update {
            it.copy(error = null, successMessage = null)
        }
    }
}