package com.ute.restauranteapp.domain.repository
import com.ute.restauranteapp.domain.model.*

interface PedidoRepository {

    suspend fun getPedidos(
        search: String? = null,
        cliente: Int? = null,
        estado: String? = null,
        totalMin: Double? = null,
        totalMax: Double? = null,
        ordering: String? = null,
    ): Result<List<Pedido>>

    suspend fun getPedido(id: Int): Result<Pedido>

    suspend fun createPedido(payload: PedidoPayload): Result<Pedido>

    suspend fun crearPedidoCompleto(
        cliente: Int,
        items: List<Pair<Int, Int>> // (platoId, cantidad)
    ): Result<Pedido>

    suspend fun updatePedido(id: Int, payload: PedidoPayload): Result<Pedido>

    suspend fun deletePedido(id: Int): Result<Unit>

    suspend fun getStats(): Result<Map<String, Any>>
}