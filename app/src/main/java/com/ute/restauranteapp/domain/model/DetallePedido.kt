package com.ute.restauranteapp.domain.model

data class DetallePedido(
    val id: Int,
    val pedido: Int,
    val plato: Int,
    val platoNombre: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double,
)

data class DetallePedidoPayload(
    val pedido: Int,
    val plato: Int,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double,
)

data class DetallePedidoFilters(
    val pedido: Int? = null,
    val plato: Int? = null,
    val cantidadMin: Int? = null,
    val cantidadMax: Int? = null,
)