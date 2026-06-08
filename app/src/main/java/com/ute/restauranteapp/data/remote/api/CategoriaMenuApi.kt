package com.ute.restauranteapp.data.remote.api


import com.ute.restauranteapp.data.remote.dto.CategoriaMenuDto
import com.ute.restauranteapp.data.remote.dto.CategoriaMenuRequestDto
import com.ute.restauranteapp.data.remote.dto.PaginatedDto
import retrofit2.Response
import retrofit2.http.*

interface CategoriaMenuApi {

    @GET("categorias/")
    suspend fun getCategorias(
        @QueryMap filters: Map<String, String>,
    ): Response<PaginatedDto<CategoriaMenuDto>>

    @GET("categorias/{id}/")
    suspend fun getCategoria(
        @Path("id") id: Int,
    ): Response<CategoriaMenuDto>

    @POST("categorias/")
    suspend fun createCategoria(
        @Body body: CategoriaMenuRequestDto,
    ): Response<CategoriaMenuDto>

    @PATCH("categorias/{id}/")
    suspend fun updateCategoria(
        @Path("id") id: Int,
        @Body body: CategoriaMenuRequestDto,
    ): Response<CategoriaMenuDto>

    @DELETE("categorias/{id}/")
    suspend fun deleteCategoria(
        @Path("id") id: Int,
    ): Response<Unit>
}