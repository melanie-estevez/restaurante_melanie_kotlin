package com.ute.restauranteapp.data.repository

import com.ute.restauranteapp.data.remote.api.CategoriaMenuApi
import com.ute.restauranteapp.data.remote.dto.toDomain
import com.ute.restauranteapp.data.remote.dto.toRequest
import com.ute.restauranteapp.data.remote.utils.safeApiCall
import com.ute.restauranteapp.domain.model.CategoriaMenuPayload
import com.ute.restauranteapp.domain.repository.CategoriaMenuRepository

import javax.inject.Inject

class CategoriaMenuRepositoryImpl @Inject constructor(
    private val api: CategoriaMenuApi
) : CategoriaMenuRepository {

    override suspend fun getCategorias(
        search: String?,
        nombre: String?,
        ordering: String?
    ) = safeApiCall {
        api.getCategorias(
            buildMap {
                search?.let { put("search", it) }
                nombre?.let { put("nombre", it) }
                ordering?.let { put("ordering", it) }
            }
        )
    }.map { paginated ->
        paginated.results.map { it.toDomain() }
    }

    override suspend fun getCategoria(id: Int) =
        safeApiCall {
            api.getCategoria(id)
        }.map { it.toDomain() }

    override suspend fun createCategoria(payload: CategoriaMenuPayload) =
        safeApiCall {
            api.createCategoria(payload.toRequest())
        }.map { it.toDomain() }

    override suspend fun updateCategoria(id: Int, payload: CategoriaMenuPayload) =
        safeApiCall {
            api.updateCategoria(id, payload.toRequest())
        }.map { it.toDomain() }

    override suspend fun deleteCategoria(id: Int) =
        safeApiCall {
            api.deleteCategoria(id)
        }
}