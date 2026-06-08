package com.ute.restauranteapp.domain.repository
import com.ute.restauranteapp.domain.model.*

interface PlatoRepository {

    suspend fun getPlatos(
        search: String? = null,
        categoria: Int? = null,
        priceMin: Double? = null,
        priceMax: Double? = null,
        isActive: Boolean? = null,
        ordering: String? = null,
    ): Result<List<Plato>>

    suspend fun getPlato(id: Int): Result<Plato>
    suspend fun createPlato(payload: PlatoPayload): Result<Plato>
    suspend fun updatePlato(id: Int, payload: PlatoPayload): Result<Plato>
    suspend fun deletePlato(id: Int): Result<Unit>

    suspend fun getStats(): Result<Map<String, Any>>
}