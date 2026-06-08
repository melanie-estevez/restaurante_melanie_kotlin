package com.ute.restauranteapp.presentation.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.restauranteapp.domain.model.Pedido
import com.ute.restauranteapp.domain.model.PedidoEstado
import com.ute.restauranteapp.domain.model.PedidoPayload
import com.ute.restauranteapp.domain.repository.PedidoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PedidoAdminUiState(
    val isLoading: Boolean = false,
    val pedidos: List<Pedido> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class PedidoAdminViewModel @Inject constructor(
    private val repository: PedidoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PedidoAdminUiState())
    val state = _state.asStateFlow()

    init {
        cargarPedidos()
    }

    fun cargarPedidos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getPedidos()
                .onSuccess { pedidos ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            pedidos = pedidos
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al cargar pedidos"
                        )
                    }
                }
        }
    }

    fun cambiarEstado(pedido: Pedido, estado: PedidoEstado) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.updatePedido(
                id = pedido.id,
                payload = PedidoPayload(
                    cliente = pedido.cliente,
                    estado = estado
                )
            ).onSuccess {
                cargarPedidos()
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Error al cambiar estado"
                    )
                }
            }
        }
    }

    fun eliminarPedido(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.deletePedido(id)
                .onSuccess {
                    cargarPedidos()
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al eliminar pedido"
                        )
                    }
                }
        }
    }
}