package com.ute.restauranteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.restauranteapp.domain.model.Plato
import com.ute.restauranteapp.domain.repository.PedidoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartItem(
    val plato: Plato,
    val quantity: Int,
)

sealed interface CheckoutState {
    data object Idle : CheckoutState
    data object Loading : CheckoutState
    data class Success(val pedidoId: Int) : CheckoutState
    data class Error(val message: String) : CheckoutState
}

@HiltViewModel
class CartViewModel @Inject constructor(
    private val pedidoRepository: PedidoRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items = _items.asStateFlow()

    val totalItems = _items.map { it.sumOf { i -> i.quantity } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val total = _items.map { list ->
        list.sumOf { it.plato.precio * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState = _checkoutState.asStateFlow()

    // ── CART ─────────────────────────────

    fun addItem(plato: Plato, quantity: Int = 1) {
        if (!plato.disponible) return

        _items.update { list ->
            val existing = list.find { it.plato.id == plato.id }

            if (existing != null) {
                list.map {
                    if (it.plato.id == plato.id)
                        it.copy(quantity = it.quantity + quantity)
                    else it
                }
            } else {
                list + CartItem(plato, quantity)
            }
        }
    }

    fun updateQuantity(platoId: Int, quantity: Int) {
        if (quantity <= 0) removeItem(platoId)

        _items.update { list ->
            list.map {
                if (it.plato.id == platoId)
                    it.copy(quantity = quantity)
                else it
            }
        }
    }

    fun removeItem(platoId: Int) {
        _items.update { it.filter { i -> i.plato.id != platoId } }
    }

    fun clearCart() {
        _items.value = emptyList()
    }

    // ── CHECKOUT REAL PARA TU BACKEND ─────────────

    fun checkout(clienteId: Int) {
        val items = _items.value

        if (items.isEmpty()) {
            _checkoutState.value = CheckoutState.Error("Carrito vacío")
            return
        }

        viewModelScope.launch {
            _checkoutState.value = CheckoutState.Loading

            val pedido = pedidoRepository.crearPedidoCompleto(
                cliente = clienteId,
                items = items.map { it.plato.id to it.quantity }
            ).getOrElse {
                _checkoutState.value = CheckoutState.Error(it.message ?: "Error al crear pedido")
                return@launch
            }

            clearCart()

            _checkoutState.value = CheckoutState.Success(pedido.id)
        }
    }
}