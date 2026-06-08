package com.ute.restauranteapp.domain.repository

import com.ute.restauranteapp.domain.model.*

interface ClienteRepository {
    suspend fun getClientes(
        search: String? = null,
        correo: String? = null,
        page: Int? = null,
    ): Result<Pair<List<Cliente>, Int>>

    suspend fun getCliente(id: Int): Result<Cliente>
    suspend fun createCliente(payload: ClientePayload): Result<Cliente>
    suspend fun updateCliente(id: Int, payload: ClientePayload): Result<Cliente>
    suspend fun deleteCliente(id: Int): Result<Unit>

}