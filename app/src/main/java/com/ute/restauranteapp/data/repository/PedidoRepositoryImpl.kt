package com.ute.restauranteapp.data.repository

import com.ute.restauranteapp.data.remote.api.PedidoApi
import com.ute.restauranteapp.data.remote.dto.CrearPedidoItemDto
import com.ute.restauranteapp.data.remote.dto.CrearPedidoRequestDto
import com.ute.restauranteapp.data.remote.dto.toDomain
import com.ute.restauranteapp.data.remote.dto.toRequest
import com.ute.restauranteapp.data.remote.utils.safeApiCall
import com.ute.restauranteapp.domain.model.PedidoPayload
import com.ute.restauranteapp.domain.repository.PedidoRepository

import javax.inject.Inject

class PedidoRepositoryImpl @Inject constructor(
    private val api: PedidoApi
) : PedidoRepository {

    override suspend fun getPedidos(
        search: String?,
        cliente: Int?,
        estado: String?,
        totalMin: Double?,
        totalMax: Double?,
        ordering: String?
    ) = safeApiCall {
        api.getPedidos(
            buildMap {
                search?.let { put("search", it) }
                cliente?.let { put("cliente", it.toString()) }
                estado?.let { put("estado", it) }
                totalMin?.let { put("total_min", it.toString()) }
                totalMax?.let { put("total_max", it.toString()) }
                ordering?.let { put("ordering", it) }
            }
        )
    }.map { paginated ->
        paginated.results.map { it.toDomain() }
    }

    override suspend fun getPedido(id: Int) =
        safeApiCall {
            api.getPedido(id)
        }.map { it.toDomain() }

    override suspend fun createPedido(payload: PedidoPayload) =
        safeApiCall {
            api.createPedido(payload.toRequest())
        }.map { it.toDomain() }

    override suspend fun crearPedidoCompleto(
        cliente: Int,
        items: List<Pair<Int, Int>>
    ) = safeApiCall {
        api.crearPedidoCompleto(
            CrearPedidoRequestDto(
                cliente = cliente,
                items = items.map { (platoId, cantidad) ->
                    CrearPedidoItemDto(
                        plato = platoId,
                        cantidad = cantidad
                    )
                }
            )
        )
    }.map { it.toDomain() }

    override suspend fun updatePedido(id: Int, payload: PedidoPayload) =
        safeApiCall {
            api.updatePedido(id, payload.toRequest())
        }.map { it.toDomain() }

    override suspend fun deletePedido(id: Int) =
        safeApiCall {
            api.deletePedido(id)
        }

    override suspend fun getStats(): Result<Map<String, Any>> {
        return Result.success(emptyMap())
    }
}