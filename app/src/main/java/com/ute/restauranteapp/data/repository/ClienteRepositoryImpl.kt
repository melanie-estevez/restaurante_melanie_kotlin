package com.ute.restauranteapp.data.repository

import com.ute.restauranteapp.data.remote.api.ClienteApi
import com.ute.restauranteapp.data.remote.dto.toDomain
import com.ute.restauranteapp.data.remote.dto.toRequest
import com.ute.restauranteapp.data.remote.utils.safeApiCall
import com.ute.restauranteapp.domain.model.ClientePayload
import com.ute.restauranteapp.domain.repository.ClienteRepository

import javax.inject.Inject

class ClienteRepositoryImpl @Inject constructor(
    private val api: ClienteApi
) : ClienteRepository {

    override suspend fun getClientes(
        search: String?,
        correo: String?,
        page: Int?
    ) = safeApiCall {
        api.getClientes(
            buildMap {
                search?.let { put("search", it) }
                correo?.let { put("correo", it) }
                page?.let { put("page", it.toString()) }
            }
        )
    }.map { paginated ->
        Pair(
            paginated.results.map { it.toDomain() },
            paginated.count
        )
    }

    override suspend fun getCliente(id: Int) =
        safeApiCall {
            api.getCliente(id)
        }.map { it.toDomain() }

    override suspend fun createCliente(payload: ClientePayload) =
        safeApiCall {
            api.createCliente(payload.toRequest())
        }.map { it.toDomain() }

    override suspend fun updateCliente(id: Int, payload: ClientePayload) =
        safeApiCall {
            api.updateCliente(id, payload.toRequest())
        }.map { it.toDomain() }

    override suspend fun deleteCliente(id: Int) =
        safeApiCall {
            api.deleteCliente(id)
        }
}