package com.ute.restauranteapp.domain.model

data class CategoriaMenu(
    val id: Int,
    val nombre: String,
    val descripcion: String,
)

data class CategoriaMenuPayload(
    val nombre: String,
    val descripcion: String,
)

data class CategoriaMenuFilters(
    val nombre: String? = null,
    val search: String? = null,
    val ordering: String? = null,
)