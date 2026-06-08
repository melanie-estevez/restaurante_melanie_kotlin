package com.ute.restauranteapp.domain.model

enum class PedidoEstado(
    val value: String,
    val label: String,
) {
    PENDIENTE("PENDIENTE", "Pendiente"),
    PREPARANDO("PREPARANDO", "Preparando"),
    ENTREGADO("ENTREGADO", "Entregado"),
    CANCELADO("CANCELADO", "Cancelado");

    companion object {
        fun fromValue(value: String): PedidoEstado =
            entries.firstOrNull { it.value == value } ?: PENDIENTE
    }
}

data class Pedido(
    val id: Int,
    val cliente: Int,
    val clienteNombre: String,
    val fechaPedido: String,
    val estado: PedidoEstado,
    val total: Double,
)

data class PedidoPayload(
    val cliente: Int,
    val estado: PedidoEstado,
)

data class PedidoFilters(
    val cliente: Int? = null,
    val estado: PedidoEstado? = null,
    val totalMin: Double? = null,
    val totalMax: Double? = null,
    val search: String? = null,
    val ordering: String? = null,
)