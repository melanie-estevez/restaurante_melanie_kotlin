package com.ute.restauranteapp.data.remote.dto
import com.google.gson.annotations.SerializedName
import com.ute.restauranteapp.domain.model.DetallePedido
import com.ute.restauranteapp.domain.model.DetallePedidoPayload

data class DetallePedidoDto(
    val id: Int,
    val pedido: Int,
    val plato: Int,

    @SerializedName("plato_nombre")
    val platoNombre: String,

    val cantidad: Int,

    @SerializedName("precio_unitario")
    val precioUnitario: Double,

    val subtotal: Double,
)

data class DetallePedidoRequestDto(
    val pedido: Int,
    val plato: Int,
    val cantidad: Int,

    @SerializedName("precio_unitario")
    val precioUnitario: Double,

    val subtotal: Double,
)

fun DetallePedidoDto.toDomain() = DetallePedido(
    id = id,
    pedido = pedido,
    plato = plato,
    platoNombre = platoNombre,
    cantidad = cantidad,
    precioUnitario = precioUnitario,
    subtotal = subtotal,
)

fun DetallePedidoPayload.toRequest() = DetallePedidoRequestDto(
    pedido = pedido,
    plato = plato,
    cantidad = cantidad,
    precioUnitario = precioUnitario,
    subtotal = subtotal,
)