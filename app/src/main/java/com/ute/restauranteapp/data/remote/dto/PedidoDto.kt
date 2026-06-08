package com.ute.restauranteapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.ute.restauranteapp.domain.model.Pedido
import com.ute.restauranteapp.domain.model.PedidoEstado
import com.ute.restauranteapp.domain.model.PedidoPayload


data class PedidoDto(
    val id: Int,
    val cliente: Int,

    @SerializedName("cliente_nombre")
    val clienteNombre: String,

    @SerializedName("fecha_pedido")
    val fechaPedido: String,

    val estado: String,
    val total: Double,
)


data class PedidoRequestDto(
    val cliente: Int,
    val estado: String,
)


data class CrearPedidoRequestDto(
    val cliente: Int,
    val items: List<CrearPedidoItemDto>,
)

data class CrearPedidoItemDto(
    val plato: Int,
    val cantidad: Int,
)


fun PedidoDto.toDomain() = Pedido(
    id = id,
    cliente = cliente,
    clienteNombre = clienteNombre,
    fechaPedido = fechaPedido,
    estado = PedidoEstado.fromValue(estado),
    total = total,
)


fun PedidoPayload.toRequest() = PedidoRequestDto(
    cliente = cliente,
    estado = estado.value,
)