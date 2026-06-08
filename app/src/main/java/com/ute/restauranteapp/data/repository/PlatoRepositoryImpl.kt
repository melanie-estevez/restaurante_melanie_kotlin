package com.ute.restauranteapp.data.repository

import com.ute.restauranteapp.data.remote.api.PlatoApi
import com.ute.restauranteapp.data.remote.dto.toDomain
import com.ute.restauranteapp.data.remote.dto.toRequest
import com.ute.restauranteapp.data.remote.utils.safeApiCall
import com.ute.restauranteapp.domain.model.PlatoPayload
import com.ute.restauranteapp.domain.repository.PlatoRepository

import javax.inject.Inject

class PlatoRepositoryImpl @Inject constructor(
    private val api: PlatoApi
) : PlatoRepository {

    override suspend fun getPlatos(
        search: String?,
        categoria: Int?,
        priceMin: Double?,
        priceMax: Double?,
        isActive: Boolean?,
        ordering: String?
    ) = safeApiCall {
        api.getPlatos(
            buildMap {
                search?.let { put("search", it) }
                categoria?.let { put("categoria", it.toString()) }
                priceMin?.let { put("precio_min", it.toString()) }
                priceMax?.let { put("precio_max", it.toString()) }
                isActive?.let { put("disponible", it.toString()) }
                ordering?.let { put("ordering", it) }
            }
        )
    }.map { paginated ->
        paginated.results.map { it.toDomain() }
    }

    override suspend fun getPlato(id: Int) =
        safeApiCall {
            api.getPlato(id)
        }.map { it.toDomain() }

    override suspend fun createPlato(payload: PlatoPayload) =
        safeApiCall {
            api.createPlato(payload.toRequest())
        }.map { it.toDomain() }

    override suspend fun updatePlato(
        id: Int,
        payload: PlatoPayload
    ) = safeApiCall {
        api.updatePlato(id, payload.toRequest())
    }.map { it.toDomain() }

    override suspend fun deletePlato(id: Int) =
        safeApiCall {
            api.deletePlato(id)
        }

    override suspend fun getStats(): Result<Map<String, Any>> {
        return Result.success(emptyMap())
    }
}