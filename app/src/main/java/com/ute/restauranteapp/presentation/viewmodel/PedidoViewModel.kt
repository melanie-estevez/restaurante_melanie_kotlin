package com.ute.restauranteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.restauranteapp.domain.model.Pedido
import com.ute.restauranteapp.domain.model.PedidoEstado
import com.ute.restauranteapp.domain.model.PedidoFilters
import com.ute.restauranteapp.domain.model.PedidoPayload
import com.ute.restauranteapp.domain.model.Plato
import com.ute.restauranteapp.domain.repository.PedidoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlatoSeleccionado(
    val plato: Plato,
    val cantidad: Int
)

sealed class PedidoState {
    object Idle : PedidoState()
    object Loading : PedidoState()
    data class Success(val message: String) : PedidoState()
    data class Error(val message: String) : PedidoState()
}

data class PedidoUiState(
    val isLoading: Boolean = false,
    val pedidos: List<Pedido> = emptyList(),
    val pedido: Pedido? = null,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class PedidoViewModel @Inject constructor(
    private val repository: PedidoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoUiState())
    val uiState: StateFlow<PedidoUiState> = _uiState.asStateFlow()

    private val _state = MutableStateFlow<PedidoState>(PedidoState.Idle)
    val state: StateFlow<PedidoState> = _state.asStateFlow()

    private val _items = MutableStateFlow<List<PlatoSeleccionado>>(emptyList())
    val items: StateFlow<List<PlatoSeleccionado>> = _items.asStateFlow()

    val total: Double
        get() = _items.value.sumOf { it.plato.precio * it.cantidad }

    fun cargarPedidos(
        filters: PedidoFilters = PedidoFilters()
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            repository.getPedidos(
                search = filters.search,
                cliente = filters.cliente,
                estado = filters.estado?.value,
                totalMin = filters.totalMin,
                totalMax = filters.totalMax,
                ordering = filters.ordering
            ).onSuccess { pedidos ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    pedidos = pedidos,
                    error = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Error al cargar pedidos"
                )
            }
        }
    }

    fun cargarPedido(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            repository.getPedido(id)
                .onSuccess { pedido ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        pedido = pedido,
                        error = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al cargar el pedido"
                    )
                }
        }
    }

    fun agregarPlato(plato: Plato) {
        val actual = _items.value.toMutableList()
        val index = actual.indexOfFirst { it.plato.id == plato.id }

        if (index >= 0) {
            val item = actual[index]
            actual[index] = item.copy(
                cantidad = item.cantidad + 1
            )
        } else {
            actual.add(
                PlatoSeleccionado(
                    plato = plato,
                    cantidad = 1
                )
            )
        }

        _items.value = actual
    }

    fun aumentarCantidad(platoId: Int) {
        _items.value = _items.value.map {
            if (it.plato.id == platoId) {
                it.copy(cantidad = it.cantidad + 1)
            } else {
                it
            }
        }
    }

    fun disminuirCantidad(platoId: Int) {
        _items.value = _items.value.mapNotNull {
            if (it.plato.id == platoId) {
                val nuevaCantidad = it.cantidad - 1

                if (nuevaCantidad <= 0) {
                    null
                } else {
                    it.copy(cantidad = nuevaCantidad)
                }
            } else {
                it
            }
        }
    }

    fun eliminarPlato(platoId: Int) {
        _items.value = _items.value.filter {
            it.plato.id != platoId
        }
    }

    fun limpiarCarrito() {
        _items.value = emptyList()
    }

    fun crearPedidoCompleto(clienteId: Int) {
        if (_items.value.isEmpty()) {
            _state.value = PedidoState.Error("Debe agregar al menos un plato")
            return
        }

        viewModelScope.launch {
            _state.value = PedidoState.Loading

            repository.crearPedidoCompleto(
                cliente = clienteId,
                items = _items.value.map {
                    it.plato.id to it.cantidad
                }
            ).onSuccess {
                limpiarCarrito()

                _state.value = PedidoState.Success(
                    "Pedido creado correctamente"
                )

                cargarPedidos()

            }.onFailure { error ->
                _state.value = PedidoState.Error(
                    error.message ?: "Error al crear pedido"
                )
            }
        }
    }

    fun cambiarEstado(
        pedido: Pedido,
        nuevoEstado: PedidoEstado
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            repository.updatePedido(
                id = pedido.id,
                payload = PedidoPayload(
                    cliente = pedido.cliente,
                    estado = nuevoEstado
                )
            ).onSuccess { pedidoActualizado ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    pedido = pedidoActualizado,
                    successMessage = "Estado actualizado correctamente"
                )

                cargarPedidos()

            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Error al actualizar pedido"
                )
            }
        }
    }

    fun eliminarPedido(
        id: Int,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            repository.deletePedido(id)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Pedido eliminado correctamente"
                    )

                    cargarPedidos()
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al eliminar pedido"
                    )
                }
        }
    }

    fun limpiarEstado() {
        _state.value = PedidoState.Idle
        _uiState.value = _uiState.value.copy(
            error = null,
            successMessage = null
        )
    }
}