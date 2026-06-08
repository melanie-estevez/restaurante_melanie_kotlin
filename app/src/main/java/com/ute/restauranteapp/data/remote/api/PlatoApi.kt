package com.ute.restauranteapp.data.remote.api

import com.ute.restauranteapp.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface PlatoApi {

    @GET("platos/")
    suspend fun getPlatos(
        @QueryMap filters: Map<String, String>,
    ): Response<PaginatedDto<PlatoDto>>

    @GET("platos/{id}/")
    suspend fun getPlato(
        @Path("id") id: Int,
    ): Response<PlatoDto>

    @POST("platos/")
    suspend fun createPlato(
        @Body body: PlatoRequestDto,
    ): Response<PlatoDto>

    @PATCH("platos/{id}/")
    suspend fun updatePlato(
        @Path("id") id: Int,
        @Body body: PlatoRequestDto,
    ): Response<PlatoDto>

    @DELETE("platos/{id}/")
    suspend fun deletePlato(
        @Path("id") id: Int,
    ): Response<Unit>
}