package com.ute.restauranteapp.domain.model

data class Plato(
    val id: Int,
    val categoria: Int,
    val categoriaNombre: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val disponible: Boolean,
)

data class PlatoPayload(
    val categoria: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val disponible: Boolean,
)

data class PlatoFilters(
    val nombre: String? = null,
    val categoria: Int? = null,
    val disponible: Boolean? = null,
    val precioMin: Double? = null,
    val precioMax: Double? = null,
    val search: String? = null,
    val ordering: String? = null,
)