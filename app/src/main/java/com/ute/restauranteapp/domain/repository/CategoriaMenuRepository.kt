package com.ute.restauranteapp.domain.repository

import com.ute.restauranteapp.domain.model.*

interface CategoriaMenuRepository {
    suspend fun getCategorias(
        search: String? = null,
        nombre: String? = null,
        ordering: String? = null,
    ): Result<List<CategoriaMenu>>

    suspend fun getCategoria(id: Int): Result<CategoriaMenu>
    suspend fun createCategoria(payload: CategoriaMenuPayload): Result<CategoriaMenu>
    suspend fun updateCategoria(id: Int, payload: CategoriaMenuPayload): Result<CategoriaMenu>
    suspend fun deleteCategoria(id: Int): Result<Unit>
}