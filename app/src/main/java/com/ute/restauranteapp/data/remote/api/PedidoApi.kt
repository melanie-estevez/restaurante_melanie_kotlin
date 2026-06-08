package com.ute.restauranteapp.data.remote.api

import com.ute.restauranteapp.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*
interface PedidoApi {

    @GET("pedidos/")
    suspend fun getPedidos(
        @QueryMap filters: Map<String, String>,
    ): Response<PaginatedDto<PedidoDto>>

    @GET("pedidos/{id}/")
    suspend fun getPedido(
        @Path("id") id: Int,
    ): Response<PedidoDto>

    @POST("pedidos/")
    suspend fun createPedido(
        @Body body: PedidoRequestDto,
    ): Response<PedidoDto>

    @PATCH("pedidos/{id}/")
    suspend fun updatePedido(
        @Path("id") id: Int,
        @Body body: PedidoRequestDto,
    ): Response<PedidoDto>

    @DELETE("pedidos/{id}/")
    suspend fun deletePedido(
        @Path("id") id: Int,
    ): Response<Unit>

    @POST("pedidos/crear-completo/")
    suspend fun crearPedidoCompleto(
        @Body body: CrearPedidoRequestDto,
    ): Response<PedidoDto>
}